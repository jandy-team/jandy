package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.exception.IllegalBuildNumberException;
import io.jandy.exception.ProjectNotRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Service
public class BuildService {
  private static final Logger LOGGER = LoggerFactory.getLogger(BuildService.class);

  private TravisClient travisClient = new TravisClient();
  @Autowired
  private BuildRepository buildRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private CommitRepository commitRepository;

  @Transactional
  public Build saveBuildInfo(long buildId) {
    Build build = null;
    try {
      boolean checked = false;
      while (!checked) {
        TravisClient.Result result = travisClient.getBuild(buildId);
        String state = String.valueOf(result.getBuild().get("state"));

        if ("failed".equals(state) || "passed".equals(state)) {
          build = buildRepository.findByTravisBuildId(buildId);
          build.setState(BuildState.valueOf(state.toUpperCase()));
          build.setCommit(commitRepository.save(result.getCommit()));
          build.setStartedAt(String.valueOf(result.getBuild().get("started_at")));
          build.setFinishedAt(String.valueOf(result.getBuild().get("finished_at")));
          build.setDuration(Number.class.cast(result.getBuild().get("duration")).longValue());
          build = buildRepository.save(build);

          checked = true;
        } else if ("created".equals(state) || "started".equals(state)) {
          Thread.sleep(1);
        } else {
          throw new IllegalStateException();
        }
      }

      LOGGER.trace("Save the build information{buildId: {}}", buildId);
    } catch(InterruptedException | IOException | IllegalStateException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return build;
  }

  public Build getPrev(Build build) {
    long number = build.getNumber() - 1;

    if (number == 0)
      throw new IllegalBuildNumberException();

    return buildRepository.findByNumberAndBranch_Id(number, build.getBranch().getId());
  }

  public Build getBuildForTravis(long travisBuildId) throws ProjectNotRegisteredException {
    Build build = buildRepository.findByTravisBuildId(travisBuildId);
    return build;
  }

  @Transactional
  public Build createBuild(String ownerName, String repoName, String branchName,
                           long travisBuildId, String language, Long buildNum) throws ProjectNotRegisteredException {
    Branch branch = getBranch(ownerName, repoName, branchName);

    Build build = new Build();
    build.setBranch(branch);
    build.setTravisBuildId(travisBuildId);
    build.setLanguage(language);
    build.setNumber(buildNum);

    return buildRepository.save(build);
  }

  private Branch getBranch(String ownerName, String repoName, String branchName) throws ProjectNotRegisteredException {
    Branch branch = branchRepository.findByNameAndProject_AccountAndProject_Name(branchName, ownerName, repoName);
    return branch == null ? createBranch(ownerName, repoName, branchName) : branch;
  }

  @Transactional
  private Branch createBranch(String ownerName, String repoName, String branchName) throws ProjectNotRegisteredException {
    Project project = projectRepository.findByAccountAndName(ownerName, repoName);

    if (project == null || project.getUser() == null)
      throw new ProjectNotRegisteredException();

    Branch branch = new Branch();
    branch.setName(branchName);
    branch.setProject(project);

    return branchRepository.save(branch);
  }
}
