package io.jandy.config;

import io.jandy.service.UserService;
import io.jandy.web.interceptor.SpringEnvironmentsAttributeInterceptor;
import io.jandy.web.interceptor.UserCheckingInterceptor;
import io.jandy.web.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

  @Autowired
  private UserInterceptor userInterceptor;

  @Autowired
  private UserCheckingInterceptor userCheckingInterceptor;

  @Autowired
  private SpringEnvironmentsAttributeInterceptor springEnvironmentsAttributeInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addWebRequestInterceptor(springEnvironmentsAttributeInterceptor);
    registry.addInterceptor(userInterceptor)
        .excludePathPatterns("/");
    registry.addInterceptor(userCheckingInterceptor);
  }

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/signin").setViewName("forward:/");
    registry.addViewController("/signout");
  }
}
