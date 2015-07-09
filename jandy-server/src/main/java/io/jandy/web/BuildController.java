package io.jandy.web;

import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author JCooky
 * @since 2015-07-10
 */
@Controller
@RequestMapping("/build")
public class BuildController {
  @Autowired
  private BuildRepository buildRepository;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ModelAndView build(@PathVariable long id) {
    Build build = buildRepository.findOne(id);

    return new ModelAndView("build")
        .addObject("build", build);
  }
}
