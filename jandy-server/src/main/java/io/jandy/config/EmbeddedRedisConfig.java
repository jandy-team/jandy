package io.jandy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author JCooky
 * @since 2016-04-07
 */
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class EmbeddedRedisConfig {

  @Autowired
  private Environment env;

  @Autowired
  private RedisProperties redisProperties;

  @PostConstruct
  public void init() throws IOException {
    String redisType = env.getProperty("spring.redis.type");
    Integer redisPort = redisProperties.getPort();

    if ("embedded".equals(redisType)) {
      RedisServer redisServer = new RedisServer(redisPort);
      redisServer.start();
    }
  }
}
