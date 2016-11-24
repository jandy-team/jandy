package io.jandy.config;

import io.jandy.web.interceptor.UserInsertInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Configuration
@Profile("!test")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

  @Autowired
  private UserInsertInterceptor userInsertInterceptor;


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userInsertInterceptor).addPathPatterns("/**");
  }

}
