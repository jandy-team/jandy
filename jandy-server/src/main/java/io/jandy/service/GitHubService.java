package io.jandy.service;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import io.jandy.config.social.SecurityContext;
import io.jandy.exception.NotSignedInException;
import org.apache.commons.io.FileUtils;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttpConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * @author JCooky
 * @since 2015-07-01
 */
@Service
public class GitHubService {

  static {
    new File(FileUtils.getTempDirectory(), "/jandy/cache").mkdirs();
  }

  @Autowired
  private ApplicationContext context;

  public GitHub getGitHub() throws NotSignedInException, IOException {
    Cache cache = new Cache(new File(FileUtils.getTempDirectory(), "/jandy/cache"), 10 * 1024 * 1024);
    GitHubBuilder builder = GitHubBuilder.fromEnvironment()
        .withConnector(new OkHttpConnector(new OkUrlFactory(new OkHttpClient().setCache(cache))));

    if (SecurityContext.userSignedIn()) {
      ConnectionRepository connectionRepository = context.getBean(ConnectionRepository.class);
      Connection<?> connection = connectionRepository.findPrimaryConnection(org.springframework.social.github.api.GitHub.class);
      builder.withOAuthToken(connection.createData().getAccessToken());
    }

    return builder.build();
  }

  public GHMyself getUser() throws NotSignedInException, IOException {
    return getGitHub().getMyself();
  }

  public GHUser getUser(String login) throws NotSignedInException, IOException {
    return getGitHub().getUser(login);
  }

}
