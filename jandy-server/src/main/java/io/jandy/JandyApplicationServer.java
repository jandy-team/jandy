package io.jandy;

import com.mysema.query.jpa.impl.JPAQueryFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.inject.Provider;
import javax.persistence.EntityManager;

/**
 * Created by JCooky on 15. 6. 29..
 */
@SpringBootApplication
@EnableAsync
public class JandyApplicationServer implements CommandLineRunner {
  private final Logger logger = LoggerFactory.getLogger(JandyApplicationServer.class);

  @Bean
  public JPAQueryFactory jpaQueryFactory(Provider<EntityManager> em) {
    return new JPAQueryFactory(em);
  }

  @Bean
  public DomainClassConverter domainClassConverter(ConversionService conversionService) {
    return new DomainClassConverter(conversionService);
  }

  @Override
  public void run(String... args) throws Exception {
    String version = IOUtils.toString(Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("VERSION"));
    logger.info("Launching JandyApplication v{}", version);
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .listeners(new ApplicationPidFileWriter())
        .sources(JandyApplicationServer.class)
        .run(args);
  }
}
