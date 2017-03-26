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
public abstract class ProfService {
  private static final Logger log = LoggerFactory.getLogger(ProfService.class);

  @Autowired
  private ProfTreeNodeRepository profTreeNodeRepository;
  @Autowired
  private ProfThreadRepository profThreadRepository;

  @Autowired
  private BuildRepository buildRepository;

  @Autowired
  private ProfExceptionRepository profExceptionRepository;
  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;

  private TravisClient travisClient = new TravisClient();
  @Autowired
  private CommitRepository commitRepository;


  @Transactional
  public void doSaveProf(ProfilingContext profilingContext) throws InterruptedException {
    long profId = profilingContext.getProfId();

    long processStartTime = Long.MAX_VALUE, processEndTime = 0;
    ProfContextDump prof = profContextDumpRepository.findOne(profId);
    for (ThreadObject to : profilingContext.getThreadObjects()) {
      ProfThread profThread = new ProfThread();
      profThread.setThreadId(to.getThreadId());
      profThread.setThreadName(to.getThreadName());
      profThread.setRoot(profTreeNodeRepository.findOne(to.getRootId()));
      profThread.setProfContext(prof);
      profThread = profThreadRepository.save(profThread);

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

      prof.getThreads().add(profThread);
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

  protected abstract void doUpdateTreeNodes0(List<TreeNode> treeNodes);

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