package io.jandy.web;

import com.google.common.collect.Iterables;
import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.exception.UserNotFoundException;
import io.jandy.util.api.GitHubApi;
import io.jandy.service.UserService;
import io.jandy.util.api.json.GHOrg;
import io.jandy.util.api.json.GHRepo;
import io.jandy.util.api.json.GHUser;
import io.jandy.util.ColorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.lowerCase;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
  private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

  @Autowired
  private UserService userService;
  @Autowired
  private GitHubApi gitHubApi;
  @Autowired
  private ProjectRepository projectRepository;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() throws IOException, UserNotFoundException, InvocationTargetException, IllegalAccessException {

    if (userService.isAnonymous())
      return new ModelAndView("redirect:/");

    logger.debug("calling page 'index' ");

    GHUser ghUser = gitHubApi.getUser();
    List<GHOrg> organizations = gitHubApi.getUserOrgs(ghUser.getLogin()).stream()
        .map(org -> gitHubApi.getOrg((String) org.getLogin()))
        .collect(Collectors.toList());
    logger.trace("fetch from github, data: {}", organizations);


    Map<String, List<GHRepo>> repositories = new LinkedHashMap<>();
    repositories.put(ghUser.getLogin(), gitHubApi.getUserRepos(ghUser.getLogin()));
    for (GHOrg org : gitHubApi.getUserOrgs(ghUser.getLogin())) {
      repositories.put(org.getLogin(), gitHubApi.getOrgRepos(org.getLogin()));
    }
    logger.trace("get repositories: {}", repositories);

    List<String> randomColors = ColorUtils.getRandomColors(organizations.size() + 1);
    Map<String, Object> colors = new HashMap<>();
    for (int i = 0; i < organizations.size(); ++i) {
      colors.put(lowerCase(organizations.get(i).getLogin()), randomColors.get(i));
    }

    User user = userService.getSelf();
    logger.trace("get user");

    colors.put(lowerCase(ghUser.getLogin()), Iterables.getLast(randomColors));
    logger.trace("make random colors: {}", colors);

    Set<Long> imported = user.getProjects().stream()
        .map(Project::getGitHubId)
        .collect(Collectors.toSet());
    logger.trace("imported: {}", imported);

    return new ModelAndView("profile")
        .addObject("colors", colors)
        .addObject("user", user)
        .addObject("organizations", organizations)
        .addObject("repositories", repositories)
        .addObject("imported", imported)
        ;
  }

  @RequestMapping(value = "/project", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public void importProject(@RequestBody Map<String, ?> req) throws IOException, UserNotFoundException {
    String[] strs = StringUtils.split((String) req.get("fullName"), '/');
    String account = strs[0].trim(), name = strs[1].trim();

    Project project = projectRepository.findByAccountAndName(account, name);
    if (project == null) {
      GHRepo repository = gitHubApi.getRepo(account, name);
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

}
