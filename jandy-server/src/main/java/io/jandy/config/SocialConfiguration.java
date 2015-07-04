package io.jandy.config;

import io.jandy.config.social.SecurityContext;
import io.jandy.config.social.SimpleConnectionSignUp;
import io.jandy.config.social.SimpleSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.connect.GitHubConnectionFactory;

import javax.sql.DataSource;

/**
 * @author JCooky
 * @since 2015-07-03
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
  @Autowired
  private DataSource dataSource;
  @Autowired
  private SimpleConnectionSignUp simpleConnectionSignUp;
  @Autowired
  private SimpleSignInAdapter simpleSignInAdapter;

  @Override
  public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
    cfConfig.addConnectionFactory(new GitHubConnectionFactory(env.getProperty("spring.social.github.clientId"), env.getProperty("spring.social.github.clientSecret")));
  }

  /**
   * Singleton data access object providing access to connections across all users.
   */
  @Override
  public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
    JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    repository.setConnectionSignUp(this.simpleConnectionSignUp);
    return repository;
  }

  public UserIdSource getUserIdSource() {
    return new UserIdSource() {
      @Override
      public String getUserId() {
        return Long.toString(SecurityContext.getCurrentUser().getId());
      }
    };
  }

  @Bean
  @Scope(value="request", proxyMode= ScopedProxyMode.INTERFACES)
  public GitHub gitHub(ConnectionRepository repository) {
    Connection<GitHub> connection = repository.findPrimaryConnection(GitHub.class);
    return connection != null ? connection.getApi() : null;
  }

  @Bean
  public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
    return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, this.simpleSignInAdapter);
  }
}
