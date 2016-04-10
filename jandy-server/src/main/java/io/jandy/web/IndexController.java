package io.jandy.web;

import io.jandy.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2016-02-11
 */
@Controller
public class IndexController {
  @Autowired
  private GitHubService gitHubService;

  @RequestMapping(value = "/")
  public String index() throws IOException {

    if (!gitHubService.isAnonymous()) {
      return "forward:/repos";
    } else {
      return "index";
    }
  }
}
