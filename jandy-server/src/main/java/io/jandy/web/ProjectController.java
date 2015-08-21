package io.jandy.web;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mysema.query.support.Expressions;
import io.jandy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Controller
@RequestMapping("/repos")
public class ProjectController {

  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() throws Exception {
    List<Project> projects = projectRepository.findAll(new PageRequest(0, 10)).getContent();

    if (projects.isEmpty()) {
      return new ModelAndView("index");
    } else {
      Project project = projects.get(0);
      return getRepo(project.getAccount(), project.getName());
    }
  }

  @RequestMapping(value = "/{account}/{name}", method = RequestMethod.GET)
  public ModelAndView getRepo(@PathVariable String account, @PathVariable String name) throws Exception {
    Project project = projectRepository.findByAccountAndName(account, name);
    Branch master = branchRepository.findByNameAndProject_Id("master", project.getId());

    return new ModelAndView("repos")
        .addObject("project", project)
        .addObject("master", master);
  }

  @RequestMapping(value = "/{account}/{projectName}/{branchName}.svg", method = RequestMethod.GET)
  public ModelAndView getBadge(@PathVariable String account, @PathVariable String projectName, @PathVariable String branchName) throws Exception {
    QBuild b = QBuild.build;
    Page<Build> buildPage = buildRepository.findAll(b.branch.name.eq(branchName).and(b.branch.project.account.eq(account)).and(b.branch.project.name.eq(projectName)), new QPageRequest(0, 2, b.number.desc()));
    Build latest = buildPage.getContent().get(0), old = buildPage.getContent().get(1);

    long durationInNanoSeconds = latest.getJavaProfilingDump().getMaxTotalDuration() - old.getJavaProfilingDump().getMaxTotalDuration();
    SmallTime t = format(durationInNanoSeconds);

    return new ModelAndView(durationInNanoSeconds < 0 ? "yellow-badge" : "green-badge")
        .addObject("duration", t.value)
        .addObject("unit", t.unit);
  }

  private static SmallTime format (long value) {
    SmallTime t = new SmallTime();

    int loop = 0;
    while (value >= 1000) {
      value *= 0.001;
      loop++;
    }

    switch(loop) {
      case 0: t.unit = "ns"; break;
      case 1: t.unit = "µs"; break;
      case 2: t.unit = "ms"; break;
      case 3: t.unit = "s"; break;
      default: throw new IllegalArgumentException();
    }
    t.value = value;

    return t;
  }

  private static class SmallTime {
    public long value;
    public String unit;
  }
}
