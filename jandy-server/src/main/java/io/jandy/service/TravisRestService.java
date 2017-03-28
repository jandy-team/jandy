package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.*;
import io.jandy.domain.data.BuildInfo;
import io.jandy.domain.data.ProfilingContext;
import io.jandy.domain.data.ProfilingInfo;
import io.jandy.domain.data.TreeNode;
import io.jandy.util.worker.JandyTask;
import io.jandy.util.worker.JandyWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private JandyWorker jandyWorker;

  @Transactional
  public void begin(BuildInfo bi) {
    Project project = projectRepository.findByAccountAndName(bi.getOwnerName(), bi.getRepoName());

    Branch branch = branchRepository.findByNameAndProject_Id(bi.getBranchName(), project.getId());
    if (branch == null) {
      branch = new Branch();
      branch.setName(bi.getBranchName());
      branch.setProject(project);
      branch = branchRepository.save(branch);
    }

    Build build = buildRepository.findByTravisBuildId(bi.getBuildId());
    if (build != null) {
      profContextDumpRepository.delete(build.getProfiles());
      sampleRepository.delete(build.getSamples());
      buildRepository.delete(build);
    }

    build = new Build();
    build.setLanguage(bi.getLang());
    build.setTravisBuildId(bi.getBuildId());
    build.setBranch(branch);
    build.setNumber(bi.getBuildNum());
    build.setNumSamples(bi.getNumSamples());
    build = buildRepository.save(build);

    jandyWorker.start(bi.getBuildId(), ProfService.class);

    logger.info("start build for profiling: {}/{} -> {}", bi.getOwnerName(), bi.getRepoName(), bi.getBuildId());
  }

  @Transactional
  public Map<String, ?> createProf(ProfilingInfo profParams) {
    Build build = buildRepository.findByTravisBuildId(profParams.getBuildId());
    Project project = projectRepository.findByBuild(build);
    Sample sample = sampleRepository.findByNameAndProject_Id(profParams.getSampleName(), project.getId());
    if (sample == null) {
      sample = new Sample();
      sample.setName(profParams.getSampleName());
      sample.setProject(project);
      sample = sampleRepository.save(sample);
    }

    sample.getBuilds().add(build);
    sample = sampleRepository.save(sample);
    build.getSamples().add(sample);
    build = buildRepository.save(build);

    ProfContextDump prof = new ProfContextDump();
    prof.setBuild(build);
    prof.setSample(sample);
    prof.setState(ProfContextState.CREATED);
    prof = profContextDumpRepository.save(prof);

    logger.info("Create Profiling: {}", prof.getId());

    return ImmutableMap.of("profId", prof.getId());
  }

  public void saveProf(ProfilingContext profilingContext) throws Exception {
    Build build = profContextDumpRepository.findOne(profilingContext.getProfId()).getBuild();
    jandyWorker.put(build.getTravisBuildId(), JandyTask.SAVE, profilingContext);
  }

  public void updateTreeNodes(List<TreeNode> treeNodes) throws Exception {
    if (treeNodes.size() == 0)
      return ;

    Build build = profContextDumpRepository.findOne(treeNodes.get(0).getProfId())
        .getBuild();

    logger.info("update about build id: {}", build.getId());

    jandyWorker.put(build.getTravisBuildId(), JandyTask.UPDATE, treeNodes);
  }

  public void finish(long buildId) throws Exception {
    jandyWorker.put(buildId, JandyTask.FINISH, Long.valueOf(buildId));
  }

}
