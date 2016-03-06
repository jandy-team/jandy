package io.jandy.web;

import io.jandy.config.social.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author JCooky
 * @since 2016-02-11
 */
@Controller
public class IndexController {
  @RequestMapping(value = "/")
  public String index() {

    if (SecurityContext.userSignedIn()) {
      return "forward:/repos";
    } else {
      return "index";
    }
  }
}
