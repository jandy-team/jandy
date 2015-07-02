package io.jandy.config;

import io.jandy.web.intercept.UserCheckingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
  @Autowired
  private UserCheckingInterceptor userCheckingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addWebRequestInterceptor(userCheckingInterceptor);
  }
}
