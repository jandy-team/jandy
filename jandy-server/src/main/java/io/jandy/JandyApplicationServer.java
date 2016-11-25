package io.jandy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Provider;
import javax.persistence.EntityManager;

/**
 * Created by JCooky on 15. 6. 29..
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class JandyApplicationServer implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(JandyApplicationServer.class);

  @Autowired
  private ApplicationContext applicationContext;

  @Bean
  public JPAQueryFactory jpaQueryFactory(Provider<EntityManager> em) {
    return new JPAQueryFactory(em);
  }

  @Bean
  @Profile("!jpatest")
  public DomainClassConverter domainClassConverter(ConversionService conversionService) {
    return new DomainClassConverter(conversionService);
  }

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(Runtime.getRuntime().availableProcessors()-1);
    return taskExecutor;
  }

  @Override
  public void run(String... args) throws Exception {
    String version = IOUtils.toString(applicationContext.getResource("classpath:VERSION").getInputStream());
    logger.info("Launching JandyApplication v{}", version);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .listeners(new ApplicationPidFileWriter())
        .sources(JandyApplicationServer.class)
        .run(args);
  }
}
