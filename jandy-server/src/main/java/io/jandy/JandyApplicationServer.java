package io.jandy;

import com.google.common.collect.Lists;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import io.jandy.domain.Project;
import io.jandy.web.util.UserCookieGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by JCooky on 15. 6. 29..
 */
@SpringBootApplication
@EnableAsync
@EnableSocial
public class JandyApplicationServer {

  @Bean
  public JPAQueryFactory jpaQueryFactory(Provider<EntityManager> em) {
    return new JPAQueryFactory(em);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .listeners(new ApplicationPidFileWriter())
        .sources(JandyApplicationServer.class)
        .run(args);
  }
}
