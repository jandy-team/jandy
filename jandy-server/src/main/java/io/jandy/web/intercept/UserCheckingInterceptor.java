package io.jandy.web.intercept;

import io.jandy.exception.UserNotFoundException;
import io.jandy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author user1
 * @date 2015-06-30
 */
@Component
public class UserCheckingInterceptor implements WebRequestInterceptor {
  @Autowired
  private UserService userService;

  @Override
  public void preHandle(WebRequest request) throws Exception {

  }

  @Override
  public void postHandle(WebRequest request, ModelMap model) throws Exception {
    if ((model != null && !model.containsKey("user"))
        && request.getAttribute("user", RequestAttributes.SCOPE_REQUEST) == null) {
      try {
        request.setAttribute("user", userService.getSelf(), RequestAttributes.SCOPE_REQUEST);
      } catch (UserNotFoundException ex) {
        request.setAttribute("user", null, RequestAttributes.SCOPE_REQUEST);
      }
    }
  }

  @Override
  public void afterCompletion(WebRequest request, Exception ex) throws Exception {

  }
}
