package io.jandy.web.interceptor;

import io.jandy.config.social.SecurityContext;
import io.jandy.exception.NotSignedInException;
import io.jandy.exception.UserNotFoundException;
import io.jandy.service.UserService;
import io.jandy.web.util.UserCookieGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JCooky
 * @since 2015-07-04
 */
@Component
public class UserCheckingInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  private UserCookieGenerator userCookieGenerator = new UserCookieGenerator();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
    String userId = userCookieGenerator.readCookieValue(request);
    try {
      if (!StringUtils.isEmpty(userId) && !mav.getModelMap().containsAttribute("user")) {
        mav.addObject("user", userService.getUser(Long.parseLong(userId)));
      }
    } catch(NotSignedInException e) {
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}
