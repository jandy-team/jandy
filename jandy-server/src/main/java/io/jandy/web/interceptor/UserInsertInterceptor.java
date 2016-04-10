package io.jandy.web.interceptor;

import io.jandy.domain.User;
import io.jandy.service.UserService;
import io.jandy.service.data.GHUser;
import org.hsqldb.ClientConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2ClientConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JCooky
 * @since 2016-04-04
 */
@Component
public class UserInsertInterceptor implements HandlerInterceptor {
  @Autowired
  private UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    User self = null;
    if (!request.getRequestURI().endsWith("/login") && !userService.isAnonymous())
      self = userService.getSelf();

    request.setAttribute("self", self);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

  }
}
