package io.jandy.web;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.exception.NotSignedInException;
import io.jandy.exception.UserNotFoundException;
import io.jandy.service.GitHubService;
import io.jandy.service.UserService;
import io.jandy.web.view.model.VmRepository;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserService userService;
  @Autowired
  private GitHubService gitHubService;
  @Autowired
  private ProjectRepository projectRepository;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() throws IOException, UserNotFoundException, NotSignedInException {
    return index(gitHubService.getUser().getLogin());
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.GET)
  public ModelAndView index(@PathVariable String login) throws IOException, UserNotFoundException, NotSignedInException {

    logger.trace("calling page 'index' ");

    List<Map<String, Object>> organizations = Lists.newArrayList(Iterables.transform(gitHubService.getOrganizationService().getOrganizations(login), org -> {
          return ImmutableMap.<String, Object>builder()
              .put("login", org.getLogin())
              .put("publicRepos", org.getPublicRepos())
              .build();
        }
    ));
    logger.trace("fetch from github, data: {}", organizations);

    RepositoryService repositoryService = gitHubService.getRepositoryService();
    Map<String, List<Repository>> repositories = new HashMap<>();
    repositories.put(login, Lists.newArrayList(transformFromRepositories(repositoryService.getRepositories(login))));
    for (Map<String, Object> org : organizations) {
      repositories.put((String) org.get("login"), Lists.newArrayList(transformFromRepositories(repositoryService.getOrgRepositories((String) org.get("login")))));
    }

    return new ModelAndView("profile")
        .addObject("user", userService.getUser(login))
        .addObject("organizations", organizations)
        .addObject("repositories", repositories)
        ;
  }

  @RequestMapping(value = "/project", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void importProject(@RequestBody Map<String, ?> req) throws IOException, NotSignedInException, UserNotFoundException {
    String []strs = StringUtils.split((String)req.get("fullName"), '/');
    String account = strs[0].trim(), name = strs[1].trim();

    Project project = projectRepository.findByAccountAndName(account, name);
    if (project == null) {
      Repository repository = gitHubService.getRepositoryService().getRepository(account, name);
      project = new Project();
      project.setAccount(account);
      project.setGitHubId(repository.getId());
      project.setName(name);
      project.setUser(userService.getSelf());
      logger.trace("make new project: {} and save it", project.toString());
    } else {
      User user = userService.getSelf();
      project.setUser(user);
      logger.trace("enable project: {}", project.toString());
    }
    projectRepository.save(project);
  }

  @RequestMapping(value = "/project/{githubId}", method = RequestMethod.DELETE)
  @ResponseBody
  public void disableProject(@PathVariable long githubId) throws IOException, UserNotFoundException {
    Project project = projectRepository.findByGitHubId(githubId);
    project.setUser(null);
    projectRepository.save(project);
  }

  private Iterable<VmRepository> transformFromRepositories(Iterable<Repository> repository) throws IOException, UserNotFoundException {
    final User user = userService.getSelf();
    return Iterables.transform(repository, repo -> {
      try {
        VmRepository vmRepo = new VmRepository(repo);
        Project project = this.projectRepository.findByGitHubId(vmRepo.getId());
        if (project != null && user.getProjects().contains(project)) {
          vmRepo.setImported(true);
        }
        return vmRepo;
      } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
