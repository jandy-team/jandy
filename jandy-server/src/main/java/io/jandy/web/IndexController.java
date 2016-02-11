package io.jandy.web;

import io.jandy.web.util.UserCookieGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JCooky
 * @since 2016-02-11
 */
@Controller
public class IndexController {
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index(HttpServletRequest request) {
    UserCookieGenerator userCookieGenerator = new UserCookieGenerator();
    if (!StringUtils.isEmpty(userCookieGenerator.readCookieValue(request))) {
      return "forward:/repos";
    } else {
      return "index";
    }
  }
}
