package io.jandy.web;

import io.jandy.domain.*;
import io.jandy.exception.ResourceNotFoundException;
import io.jandy.service.GitHubService;
import io.jandy.util.SmallTime;
import org.kohsuke.github.GHUser;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/repos")
public class ProjectController {
  private final Logger logger = LoggerFactory.getLogger(ProjectController.class);

  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private BuildRepository buildRepository;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  private FreeMarkerConfigurer configurer;
  @Autowired
  private GitHubService github;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() throws Exception {
    List<Project> projects = projectRepository.findAll(new PageRequest(0, 1)).getContent();

    if (projects.isEmpty()) {
      logger.debug("Send redirect to /profile");
      return new ModelAndView("redirect:/profile");
    } else {
      return new ModelAndView("redirect:/repos/"+projects.get(0).getAccount()+"/"+projects.get(0).getName());
    }
  }

  @RequestMapping(value = "/{account}/{name}", method = RequestMethod.GET)
  public ModelAndView getRepo(@PathVariable String account, @PathVariable String name) throws Exception {
    Project project = projectRepository.findByAccountAndName(account, name);
    Branch branch = branchRepository.findByNameAndProject_Id("master", project.getId());

    String url = request.getRequestURL().toString();
    url = url.substring(0, url.indexOf(request.getServletPath()));

    PrettyTime p = new PrettyTime(Locale.ENGLISH);
    branch.getBuilds().stream().forEach(b -> {
      if (b.getFinishedAt() != null)
        b.setBuildAt(p.format(DatatypeConverter.parseDateTime(b.getFinishedAt())));
      if (b.getCommit() != null) {
        GHUser user = null;
        try {
          user = github.getUser(b.getCommit().getCommitterName());
          b.getCommit().setCommitterAvatarUrl(user.getAvatarUrl());
        } catch (IOException e) {
          logger.error(e.getMessage(), e);
        }
      }
    });

    return new ModelAndView("builds")
        .addObject("projects", projectRepository.findAll())
        .addObject("project", project)
        .addObject("branch", branch)
        .addObject("url", url)
        .addObject("builds", branch.getBuilds())
        ;
  }

  @RequestMapping(value = "/{account}/{projectName}/{branchName}.svg")
  public ResponseEntity<String> getBadge(@PathVariable String account, @PathVariable String projectName, @PathVariable String branchName) throws Exception {


    QBuild b = QBuild.build;
    Page<Build> buildPage = buildRepository.findAll(b.branch.name.eq(branchName).and(b.branch.project.account.eq(account)).and(b.branch.project.name.eq(projectName)), new QPageRequest(0, 2, b.number.desc()));
    if (buildPage.getTotalPages() == 0)
      throw new ResourceNotFoundException(account+"/"+projectName+" is not built");
    Build latest = buildPage.getContent().get(0), old = buildPage.getContent().get(1);

    long durationInNanoSeconds = latest.getProfContextDump().getMaxTotalDuration() - old.getProfContextDump().getMaxTotalDuration();
    SmallTime t = SmallTime.format(Math.abs(durationInNanoSeconds));

    long current = System.currentTimeMillis();
    HttpHeaders headers = new HttpHeaders(); // see #7
    headers.setExpires(current);
    headers.setDate(current);

    return ResponseEntity
        .ok()
        .headers(headers)
        .cacheControl(CacheControl.noCache())
        .lastModified(current)
        .eTag(Long.toString(latest.getId()))
        .body(FreeMarkerTemplateUtils.processTemplateIntoString(configurer.getConfiguration().getTemplate(durationInNanoSeconds <= 0 ? "badge/green-badge.ftl" : "badge/red-badge.ftl"), t));
  }
}
