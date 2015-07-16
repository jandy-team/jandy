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

  @RequestMapping(method = RequestMethod.GET)
  public String index() {
    return "repos";
  }

}
