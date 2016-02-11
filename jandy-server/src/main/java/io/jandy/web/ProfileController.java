package io.jandy.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
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
import io.jandy.util.ColorUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBeanPropertyMapDecorator;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
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
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.StringUtils.lowerCase;

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
  public ModelAndView index() throws IOException, UserNotFoundException, NotSignedInException, InvocationTargetException, IllegalAccessException {
    return index(gitHubService.getUser().getLogin());
  }

  @RequestMapping(value = "/{login}", method = RequestMethod.GET)
  public ModelAndView index(@PathVariable String login) throws IOException, UserNotFoundException, NotSignedInException, InvocationTargetException, IllegalAccessException {

    logger.debug("calling page 'index' ");

    GHUser ghUser = gitHubService.getGitHub().getUser(login);
    List<Map<String, Object>> organizations = Lists.newArrayList(ghUser.getOrganizations().stream().map(org -> {
      try {
        return ImmutableMap.<String, Object>builder()
            .put("login", org.getLogin())
            .put("publicRepos", org.getPublicRepoCount())
            .build();
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
        throw new RuntimeException(e);
      }
    }).collect(Collectors.toList()));
    logger.trace("fetch from github, data: {}", organizations);

    User user = userService.getUser(login);
    logger.trace("get user");

    Map<String, List<GHRepository>> repositories = new LinkedHashMap<>();
    repositories.put(login, Lists.newArrayList(ghUser.getRepositories().values()));
    for (GHOrganization org : ghUser.getOrganizations()) {
      repositories.put(org.getLogin(), Lists.newArrayList(org.getRepositories().values()));
    }
    logger.trace("get repositories: {}", repositories);

    List<String> randomColors = ColorUtils.getRandomColors(organizations.size() + 1);
    Map<String, Object> colors = new HashMap<>();
    for (int i = 0; i < organizations.size(); ++i) {
      colors.put(lowerCase((String) organizations.get(i).get("login")), randomColors.get(i));
    }

    colors.put(lowerCase(login), Iterables.getLast(randomColors));
    logger.trace("make random colors: {}", colors);

    Set<String> imported = user.getProjects().stream().map((p) -> Long.toString(p.getGitHubId()))
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
  public void importProject(@RequestBody Map<String, ?> req) throws IOException, NotSignedInException, UserNotFoundException {
    String[] strs = StringUtils.split((String) req.get("fullName"), '/');
    String account = strs[0].trim(), name = strs[1].trim();

    Project project = projectRepository.findByAccountAndName(account, name);
    if (project == null) {
      GHRepository repository = gitHubService.getGitHub().getRepository(account + "/" + name);
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
