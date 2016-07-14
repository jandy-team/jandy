package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.*;
import io.jandy.domain.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2016-07-13
 */
@Service
public class TravisRestService {
  private static final Logger logger = LoggerFactory.getLogger(TravisRestService.class);

  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;
  @Autowired
  private BuildRepository buildRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private SampleRepository sampleRepository;
  @Autowired
  private ProfTreeNodeRepository profTreeNodeRepository;
  @Autowired
  private ProfThreadRepository profThreadRepository;

  @Autowired
  private EntityManager em;

  @Autowired
  private ProfService profService;

  @Transactional
  public Map<String, ?> createProf(BuildInfo profParams) {

    String[] slug = StringUtils.split(profParams.getRepoSlug(), '/');
    Project project = projectRepository.findByAccountAndName(slug[0], slug[1]);

    Branch branch = branchRepository.findByNameAndProject_Id(profParams.getBranchName(), project.getId());
    if (branch == null) {
      branch = new Branch();
      branch.setName(profParams.getBranchName());
      branch.setProject(project);
      branch = branchRepository.save(branch);
    }

    Sample sample = sampleRepository.findByNameAndProject_Id(profParams.getSampleName(), project.getId());
    if (sample == null) {
      sample = new Sample();
      sample.setName(profParams.getSampleName());
      sample.setProject(project);
      sample = sampleRepository.save(sample);
    }

    Build build = buildRepository.findByTravisBuildId(profParams.getBuildId());
    if (build == null) {
      build = new Build();
      build.setBranch(branch);
      build.setNumber(profParams.getBuildNum());
      build = buildRepository.save(build);
    }

    sample.getBuilds().add(build);
    sample = sampleRepository.save(sample);
    build.getSamples().add(sample);
    build = buildRepository.save(build);

    ProfContextDump prof = new ProfContextDump();
    prof.setBuild(build);
    prof.setSample(sample);
    prof = profContextDumpRepository.save(prof);

    logger.info("Create Profiling: {}", prof.getId());

    return ImmutableMap.of("profId", prof.getId());
  }

  @Transactional
  public void saveProf(ProfilingContext profilingContext) throws InterruptedException {
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

    prof.setMaxTotalDuration(0L);
    for (ProfThread profThread : prof.getThreads()) {
      ProfTreeNode root = profThread.getRoot();

      root.setElapsedTime(0);
      root.setStartTime(Long.MAX_VALUE);
      for (ProfTreeNode child : root.getChildren()) {
        root.setElapsedTime(root.getElapsedTime() + child.getElapsedTime());
        root.setStartTime(Math.min(child.getStartTime(), root.getStartTime()));
      }

      root = profTreeNodeRepository.save(root);

      prof.setMaxTotalDuration(Math.max(prof.getMaxTotalDuration(), root.getElapsedTime()));
    }

    prof = profContextDumpRepository.save(prof);

    logger.debug("threads: {}", prof.getThreads());
    logger.info("Finish ProfilingContext ({})", prof.getId());
  }

  @Transactional
  public ListenableFuture<Object> updateTreeNodes(List<TreeNode> treeNodes) throws IOException {
    int cnt = treeNodes.size(), i = 0;

    logger.trace("---------- START ----------- ");
    for (TreeNode node : treeNodes) {
      updateTreeNode(node);
      System.out.print(".");

      if (++i > 500) {
        em.flush();
        em.clear();
        System.out.println();
        i = 0;
      }
    }

    em.flush();
    em.clear();
    System.out.println();

    logger.trace("----------  END  ----------- ");

    logger.debug("Number of saved call nodes: {}", cnt);

    return new AsyncResult<>(null);
  }

  @Transactional
  private void updateTreeNode(TreeNode treeNodeData) {
    ProfMethod method = null;
    if (treeNodeData.getMethod() != null) {
      while (true) {
        try {
          ClassObject co = treeNodeData.getMethod().getOwner();
          ProfClass klass = profService.findClass(co.getName(), co.getPackageName());

          MethodObject mo = treeNodeData.getMethod();
          method = profService.findMethod(mo.getName(), mo.getDescriptor(), mo.getAccess(), klass);
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
    treeNode.setParent(parent);
    treeNode = profTreeNodeRepository.save(treeNode);
  }
}
