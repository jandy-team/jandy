package io.jandy.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author JCooky
 * @since 2015-07-03
 */
@Component
public class SpringEnvironmentsAttributeInterceptor implements WebRequestInterceptor {
  @Autowired
  private Environment env;

  @Override
  public void preHandle(WebRequest webRequest) throws Exception {

  }

  @Override
  public void postHandle(WebRequest request, ModelMap modelMap) throws Exception {
    request.setAttribute("github_scopes", env.getProperty("spring.social.github.scope"), RequestAttributes.SCOPE_REQUEST);
  }

  @Override
  public void afterCompletion(WebRequest webRequest, Exception e) throws Exception {

  }
}
