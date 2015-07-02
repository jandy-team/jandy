package io.jandy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author user1
 * @date 2015-06-30
 */
@Controller
@RequestMapping("/")
public class ProjectController {
  @RequestMapping
  public String index() {
    return "index";
  }
}
