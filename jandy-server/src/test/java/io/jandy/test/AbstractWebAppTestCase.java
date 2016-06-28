package io.jandy.test;

import io.jandy.JandyApplicationServer;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JandyApplicationServer.class)
@WebIntegrationTest(value = {"server.port=0", "management.port=0"}, randomPort = true)
@ActiveProfiles("test")
public abstract class AbstractWebAppTestCase {
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Configuration
  public static class TestConfig {
    @Bean
    public TaskExecutor taskExecutor() {
      return new SyncTaskExecutor();
    }
  }
}
