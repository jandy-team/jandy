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
  public Build setBuildInfo(Build build) {
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

      LOGGER.trace("Save the build information{buildId: {}}", build);
    } catch(InterruptedException | IOException | IllegalStateException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return build;
  }

  public Branch getBranch(String ownerName, String repoName, String branchName) throws ProjectNotRegisteredException {
    Branch branch = branchRepository.findByNameAndProject_AccountAndProject_Name(branchName, ownerName, repoName);
    return branch == null ? createBranch(ownerName, repoName, branchName) : branch;
  }

  @Transactional
  public Branch createBranch(String ownerName, String repoName, String branchName) throws ProjectNotRegisteredException {
    Project project = projectRepository.findByAccountAndName(ownerName, repoName);

    if (project == null || project.getUser() == null)
      throw new ProjectNotRegisteredException();

    Branch branch = new Branch();
    branch.setName(branchName);
    branch.setProject(project);

    return branchRepository.save(branch);
  }
}
