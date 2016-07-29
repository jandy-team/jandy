package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.domain.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-07-14
 */
@Service
public class ProfService {
  private static final Logger logger = LoggerFactory.getLogger(ProfService.class);

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


  @Transactional(readOnly = true)
  public ProfClass findClass(String name, String packageName) {
    ProfClass klass = profClassRepository.findByNameAndPackageName(name, packageName);
    if (klass == null) {
      klass = createClass(name, packageName);
    }

    return klass;
  }

  @Transactional
  public synchronized ProfClass createClass(String name, String packageName) {
    ProfClass klass = new ProfClass();
    klass.setName(name);
    klass.setPackageName(packageName);

    return profClassRepository.save(klass);
  }

  @Transactional(readOnly = true)
  public ProfMethod findMethod(String name, String descriptor, int access, ProfClass klass) {
    ProfMethod method = profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(name, descriptor, access, klass.getId());
    if (method == null) {
      method = createMethod(name, descriptor, access, klass);
    }

    return method;
  }

  @Transactional
  public synchronized ProfMethod createMethod(String name, String descriptor, int access, ProfClass klass) {
    ProfMethod method = new ProfMethod();
    method.setName(name);
    method.setDescriptor(descriptor);
    method.setAccess(access);
    method.setOwner(klass);
    return profMethodRepository.save(method);
  }


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

    logger.debug("threads: {}", prof.getThreads());
    logger.info("Finish ProfilingContext ({})", prof.getId());
  }


  @Transactional
  public void doUpdateTreeNodes(List<TreeNode> treeNodes) throws IOException {
    int cnt = treeNodes.size(), i = 0;

    logger.trace("---------- START ----------- ");
    for (TreeNode node : treeNodes) {
      updateTreeNode(node);
      ++i;

      if (i >= 500) {
        em.flush();
        em.clear();
        i = 0;
      }
    }

    em.flush();
    em.clear();

    logger.trace("----------  END  ----------- ");

    logger.debug("Number of saved call nodes: {}", cnt);
  }

  @Transactional
  public void updateTreeNode(TreeNode treeNodeData) {
    ProfMethod method = null;
    if (treeNodeData.getMethod() != null) {
      while (true) {
        try {
          ClassObject co = treeNodeData.getMethod().getOwner();
          ProfClass klass = this.findClass(co.getName(), co.getPackageName());

          MethodObject mo = treeNodeData.getMethod();
          method = this.findMethod(mo.getName(), mo.getDescriptor(), mo.getAccess(), klass);
          break;
        } catch (DataIntegrityViolationException e) {
          logger.warn(e.getMessage(), e);
        }
      }
    }

    ProfTreeNode parent = null;
    if (treeNodeData.getParentId() != null) {
      parent = profTreeNodeRepository.findOne(treeNodeData.getParentId());
      if (parent == null) {
        parent = new ProfTreeNode();
        parent.setId(treeNodeData.getParentId());
        parent = profTreeNodeRepository.save(parent);
      }
    }

    ProfTreeNode treeNode = profTreeNodeRepository.findOne(treeNodeData.getId());
    if (treeNode == null) {
      treeNode = new ProfTreeNode();
      treeNode.setId(treeNodeData.getId());
    }

    treeNode.setMethod(method);
    treeNode.setRoot(treeNodeData.isRoot());
    treeNode.setElapsedTime(treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getElapsedTime());
    treeNode.setStartTime(treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getStartTime());
    treeNode.setException(treeNodeData.getAcc() == null ? null : createException(treeNodeData.getAcc().getException()));
    treeNode.setParent(parent);
    treeNode = profTreeNodeRepository.save(treeNode);
  }

  private ProfException createException(ExceptionObject exception) {
    if (exception == null)
      return null;

    ProfException e = new ProfException();
    e.setMessage(exception.getMessage());
    e.setKlass(this.findClass(exception.getKlass().getName(), exception.getKlass().getPackageName()));

    return profExceptionRepository.save(e);
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

      logger.trace("Save the build information{buildId: {}}", build);
    } catch (InterruptedException | IOException | IllegalStateException e) {
      logger.error(e.getMessage(), e);
    }

    buildRepository.save(build);
  }
}