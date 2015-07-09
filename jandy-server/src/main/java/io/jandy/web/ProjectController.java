package io.jandy.web;

import com.google.common.collect.Lists;
import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
  private BuildRepository buildRepository;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView index() {
    Page<Project> page = projectRepository.findAll(new PageRequest(0, 10));
    List<Project> projects = Lists.newArrayList(page);
    return projects.isEmpty() ? new ModelAndView("repos") : this.project(projects.get(0).getAccount(), projects.get(0).getName());
  }

  @RequestMapping(value = "/{ownerName}/{repoName}", method = RequestMethod.GET)
  public ModelAndView project(@PathVariable String ownerName, @PathVariable String repoName) {
    Project project = projectRepository.findByAccountAndName(ownerName, repoName);
    List<Build> builds = buildRepository.findByBranch_Project_Id(project.getId());

    return new ModelAndView("repos")
        .addObject("project", project)
        .addObject("build", builds)
        ;
  }


}
