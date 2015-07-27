package io.jandy.web;

import com.google.common.collect.Lists;
import com.mysema.query.support.Expressions;
import io.jandy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;

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

}
