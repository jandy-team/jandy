package io.jandy.service;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import org.apache.commons.io.FileUtils;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.extras.OkHttpConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
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
  private OAuth2ClientContext clientContext;

  public GitHub getGitHub() throws IOException {
    Cache cache = new Cache(new File(FileUtils.getTempDirectory(), "/jandy/cache"), 10 * 1024 * 1024);
    GitHubBuilder builder = GitHubBuilder.fromEnvironment()
        .withConnector(new OkHttpConnector(new OkUrlFactory(new OkHttpClient().setCache(cache))));

    if (clientContext.getAccessToken() != null) {
      builder.withOAuthToken(clientContext.getAccessToken().getValue());
    }

    return builder.build();
  }

  public GHMyself getSelf() throws IOException {
    GitHub github = getGitHub();
    if (github.isAnonymous())
      return null;
    return github.getMyself();
  }

  public GHUser getUser(String login) throws IOException {
    return getGitHub().getUser(login);
  }

}
