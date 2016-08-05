package io.jandy.service;

import com.google.common.collect.Sets;
import io.jandy.domain.*;
import io.jandy.domain.data.*;
import io.jandy.util.sql.BatchUpdateQueryBuilder;
import io.jandy.util.sql.InsertQueryBuilder;

import static io.jandy.util.sql.Query.*;
import static io.jandy.util.sql.conditional.Where.eq;

import io.jandy.util.sql.SelectQueryBuilder;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author JCooky
 * @since 2016-07-14
 */
@Service
public class ProfService {
  private static final Logger log = LoggerFactory.getLogger(ProfService.class);

  @Autowired
  private ProfClassRepository profClassRepository;
  @Autowired
  private ProfMethodRepository profMethodRepository;

  @Autowired
  private ProfTreeNodeRepository profTreeNodeRepository;
  @Autowired
  private ProfThreadRepository profThreadRepository;

  @Autowired
  private BuildRepository buildRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private ProfExceptionRepository profExceptionRepository;
  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;

  private TravisClient travisClient = new TravisClient();
  @Autowired
  private CommitRepository commitRepository;

  @Autowired
  private JdbcTemplate jdbc;

  @Transactional
  public void doSaveProf(ProfilingContext profilingContext) throws InterruptedException {
    long profId = profilingContext.getProfId();

    ProfContextDump prof = profContextDumpRepository.findOne(profId);
    for (ThreadObject to : profilingContext.getThreadObjects()) {
      ProfThread profThread = new ProfThread();
      profThread.setThreadId(to.getThreadId());
      profThread.setThreadName(to.getThreadName());
      profThread.setRoot(profTreeNodeRepository.findOne(to.getRootId()));
      profThread.setProfContext(prof);
      profThread = profThreadRepository.save(profThread);

      prof.getThreads().add(profThread);
    }

    long processStartTime = Long.MAX_VALUE, processEndTime = 0;
    for (ProfThread profThread : prof.getThreads()) {
      ProfTreeNode root = profThread.getRoot();

      root.setElapsedTime(0);
      root.setStartTime(Long.MAX_VALUE);
      for (ProfTreeNode child : root.getChildren()) {
        root.setElapsedTime(root.getElapsedTime() + child.getElapsedTime());
        root.setStartTime(Math.min(child.getStartTime(), root.getStartTime()));
      }

      root = profTreeNodeRepository.save(root);
      processStartTime = Math.min(processStartTime, root.getStartTime());
      processEndTime = Math.max(processEndTime, root.getStartTime() + root.getElapsedTime());
    }

    prof.setMaxTotalDuration(processEndTime - processStartTime);
    ProfContextDump prev = profContextDumpRepository.findPrev(prof);
    prof.setElapsedDuration(prev == null ? 0L : prof.getMaxTotalDuration() - prev.getMaxTotalDuration());
    prof.setState(ProfContextState.FINISH);

    prof = profContextDumpRepository.save(prof);

    log.debug("threads: {}", prof.getThreads());
    log.info("Finish ProfilingContext ({})", prof.getId());
  }

  @Transactional
  public void doUpdateTreeNodes(List<TreeNode> treeNodes) {
    int index = 0;

    while (index < treeNodes.size()) {
      doUpdateTreeNodes0(treeNodes.subList(index, Math.min(treeNodes.size(), index + 500)));
      index += 500;
    }

    log.debug("Finish to update tree nodes");
  }

  @Transactional
  private void doUpdateTreeNodes0(List<TreeNode> treeNodes) {
    BasicFormatterImpl formatter = new BasicFormatterImpl();

    Set<MethodObject> methodObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).collect(Collectors.toSet());
    Set<ClassObject> classObjects = treeNodes.stream().map(TreeNode::getMethod).filter(Objects::nonNull).map(MethodObject::getOwner).filter(Objects::nonNull).collect(Collectors.toSet());
    List<ExceptionObject> exceptionObjects = treeNodes.stream().map(n -> n.getAcc() == null ? null : n.getAcc().getException()).filter(Objects::nonNull).collect(Collectors.toList());

    if (classObjects.size() > 0) {
      InsertQueryBuilder q = insert().ignore().into("prof_class")
          .columns("name", "package_name");
      classObjects.forEach(co -> q.value(co.getName(), co.getPackageName()));

      String sql = q.toSql();
      jdbc.update(sql);
    }

    if (methodObjects.size() > 0) {
      InsertQueryBuilder q = insert().ignore().into("prof_method")
          .columns("name", "access", "descriptor", "owner_id");
      methodObjects.forEach(mo -> q.value(mo.getName(), mo.getAccess(), mo.getDescriptor(),
          subQuery(
              select().columns("id").from("prof_class").where(
                  eq("name", mo.getOwner().getName()).and(eq("package_name", mo.getOwner().getPackageName()))
              )
          )
      ));

      String sql = q.toSql();
      jdbc.update(sql);
    }

    if (exceptionObjects.size() > 0) {
      InsertQueryBuilder q = insert().ignore().into("prof_exception").columns("id", "message", "klass_id");
      exceptionObjects.forEach(eo -> q.value(
          eo.getId(), eo.getMessage(),
          subQuery(
              select().columns("id").from("prof_class").where(
                  eq("name", eo.getKlass().getName()).and(eq("package_name", eo.getKlass().getPackageName()))
              )
          )
      ));

      String sql = q.toSql();
      jdbc.update(sql);
    }

    Set<String> ids = treeNodes.stream().map(TreeNode::getParentId).filter(Objects::nonNull).collect(Collectors.toSet());
    ids.addAll(treeNodes.stream().map(TreeNode::getId).filter(Objects::nonNull).collect(Collectors.toSet()));

    if (ids.size() > 0) {
      InsertQueryBuilder q = insert().ignore().into("prof_tree_node").columns("id", "elapsed_time", "root", "start_time");
      for (String id : ids) q.value(id, 0, 0, 0);

      String sql = q.toSql();
      jdbc.update(sql);
    }

    if (treeNodes.size() > 0) {
      BatchUpdateQueryBuilder<String> q = update("prof_tree_node");
      for (TreeNode treeNodeData : treeNodes) {
        String id = treeNodeData.getId();
        MethodObject mo = treeNodeData.getMethod();
        ExceptionObject eo = treeNodeData.getAcc() == null ? null : treeNodeData.getAcc().getException();

        q
            .set(id, "elapsed_time", treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getElapsedTime())
            .set(id, "start_time", treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getStartTime())
            .set(id, "root", treeNodeData.isRoot())
            .set(id, "exception_id", eo == null ? null : eo.getId())
            .set(id, "method_id", mo == null ? null : subQuery(
                select().columns("id").from("prof_method").where(
                    eq("access", mo.getAccess()).and(
                        eq("descriptor", mo.getDescriptor()).and(
                            eq("name", mo.getName()).and(
                                eq("owner_id", mo.getOwner() == null ? null : subQuery(
                                    select().columns("id").from("prof_class").where(
                                        eq("name", mo.getOwner().getName()).and(eq("package_name", mo.getOwner().getPackageName()))
                                    )
                                ))
                            )
                        )
                    )
                )
            ))
            .set(id, "parent_id", treeNodeData.getParentId());
      }

      String sql = q.toSql();
      jdbc.update(sql);
    }
  }

  @Transactional
  public void doFinish(long buildId) {
    Build build = buildRepository.findByTravisBuildId(buildId);
    List<ProfContextDump> profiles = profContextDumpRepository.findByBuild(build);

    build.setNumSucceededSamples((int) profiles.stream()
        .filter(s -> s.getElapsedDuration() >= 0)
        .count());

    try {
      boolean checked = false;
      while (!checked) {
        TravisClient.Result result = travisClient.getBuild(build.getTravisBuildId());
        String state = String.valueOf(result.getBuild().get("state"));

        if ("failed".equals(state) || "passed".equals(state)) {
          build.setState(BuildState.valueOf(state.toUpperCase()));
          build.setCommit(commitRepository.save(result.getCommit()));
          build.setStartedAt(String.valueOf(result.getBuild().get("started_at")));
          build.setFinishedAt(String.valueOf(result.getBuild().get("finished_at")));
          build.setDuration(Number.class.cast(result.getBuild().get("duration")).longValue());

          checked = true;
        } else if ("created".equals(state) || "started".equals(state)) {
          Thread.sleep(1);
        } else {
          throw new IllegalStateException();
        }
      }

      log.trace("Save the build information{buildId: {}}", build);
    } catch (InterruptedException | IOException | IllegalStateException e) {
      log.error(e.getMessage(), e);
    }

    buildRepository.save(build);
  }
}