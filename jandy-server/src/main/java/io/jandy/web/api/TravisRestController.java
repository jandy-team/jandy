package io.jandy.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author JCooky
 * @since 2016-06-28
 */
@Controller
@RequestMapping("/rest/travis")
public class TravisRestController {

  @RequestMapping(consumes = "*/*")
  public String index() {
    return "forward:/rest/travis/v2";
  }
}
