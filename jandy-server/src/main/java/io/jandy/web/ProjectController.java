package io.jandy.web;

import freemarker.template.TemplateException;
import io.jandy.domain.*;
import io.jandy.exception.BadgeUnknownException;
import io.jandy.exception.ProjectNotRegisteredException;
import io.jandy.service.GitHubService;
import io.jandy.service.UserService;
import io.jandy.service.data.GHUser;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

  @Autowired
  private UserService userService;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() throws Exception {
    List<Project> projects = userService.getSelf().getProjects();

    if (projects.isEmpty()) {
      logger.debug("Send redirect to /profile");
      return new ModelAndView("redirect:/profile");
    } else {
      return new ModelAndView("redirect:/repos/" + projects.get(0).getAccount() + "/" + projects.get(0).getName());
    }
  }

  @RequestMapping(value = "/{account}/{name}", method = RequestMethod.GET)
  public ModelAndView getRepo(@PathVariable String account, @PathVariable String name) throws Exception {
    Project project = projectRepository.findByAccountAndName(account, name);
    if (project == null)
      throw new ProjectNotRegisteredException();
    List<Build> builds = buildRepository.findByBranch_Project_Id(project.getId(), new Sort(Sort.Direction.DESC, "number"));

    String url = request.getRequestURL().toString();
    url = url.substring(0, url.indexOf(request.getServletPath()));

    PrettyTime p = new PrettyTime(Locale.ENGLISH);
    builds.stream().forEach(b -> {
      if (b.getFinishedAt() != null)
        b.setBuildAt(p.format(DatatypeConverter.parseDateTime(b.getFinishedAt())));
      if (b.getCommit() != null) {
        GHUser user = null;
        try{
          if(github.getUser(b.getCommit().getCommitterName()) != null){
            user = github.getUser(b.getCommit().getCommitterName());
          }else{}
        }catch (NullPointerException e){
          logger.error(e.getMessage(),e);
        }catch (Exception e ){
          logger.error(e.getMessage(),e);
        }
        // TODO This code is temporary
        b.getCommit().setCommitterAvatarUrl(user == null ? null : user.getAvatarUrl());
      }
    });

    return new ModelAndView("builds")
        .addObject("project", project)
        .addObject("url", url)
        .addObject("builds", builds)
        ;
  }

  @RequestMapping(value = "/{account}/{projectName}.svg")
  public ResponseEntity<String> getBadge(@PathVariable String account, @PathVariable String projectName) throws Exception {
    return getBadge(account, projectName, "master");
  }

  @RequestMapping(value = "/{account}/{projectName}/{branchName}.svg")
  public ResponseEntity<String> getBadge(@PathVariable String account, @PathVariable String projectName, @PathVariable String branchName) throws Exception {
    QBuild b = QBuild.build;
    Page<Build> buildPage = buildRepository.findAll(b.branch.name.eq(branchName).and(b.branch.project.account.eq(account)).and(b.branch.project.name.eq(projectName)), new QPageRequest(0, 2, b.number.desc()));
    if (buildPage.getTotalPages() == 0)
      throw new BadgeUnknownException();
    Build latest = buildPage.getContent().get(0);

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
        .body(FreeMarkerTemplateUtils.processTemplateIntoString(configurer.getConfiguration().getTemplate("badge/mybadge.ftl"), latest));
  }

  @ExceptionHandler(BadgeUnknownException.class)
  public ResponseEntity<String> getBadgeForUnknown() throws IOException, TemplateException {
    return ResponseEntity
        .ok()
        .cacheControl(CacheControl.noCache())
        .lastModified(System.currentTimeMillis())
        .body(FreeMarkerTemplateUtils.processTemplateIntoString(configurer.getConfiguration().getTemplate("badge/unknown-badge.ftl"), null));
  }
}
