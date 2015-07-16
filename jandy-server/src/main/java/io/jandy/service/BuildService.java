package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.exception.ProjectNotRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Service
public class BuildService {
  @Autowired
  private BuildRepository buildRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private ProjectRepository projectRepository;

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
    Branch branch = branchRepository.findByNameAndProject_AccountAndName(branchName, ownerName, repoName);
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
