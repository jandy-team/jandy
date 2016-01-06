package io.jandy.service;

import io.jandy.config.social.SecurityContext;
import io.jandy.exception.NotSignedInException;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.*;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.github.api.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2015-07-01
 */
@Service
public class GitHubService {
  @Autowired
  private ApplicationContext context;

  private GitHubClient getGitHubClient() throws NotSignedInException {
    GitHubClient ghClient = new GitHubClient();
    if (SecurityContext.userSignedIn()) {
      ConnectionRepository connectionRepository = context.getBean(ConnectionRepository.class);
      Connection<? extends GitHub> connection = connectionRepository.findPrimaryConnection(GitHub.class);
      ghClient.setOAuth2Token(connection.createData().getAccessToken());
    }

    return ghClient;
  }

  public org.eclipse.egit.github.core.service.UserService getUserService() throws NotSignedInException {
    return new UserService(getGitHubClient());
  }

  public RepositoryService getRepositoryService() throws NotSignedInException {
    return new RepositoryService(getGitHubClient());
  }

  public OrganizationService getOrganizationService() throws NotSignedInException {
    return new OrganizationService(getGitHubClient());
  }

  public User getUser() throws NotSignedInException, IOException {
    UserService userService = getUserService();
    return userService.getUser();
  }

  public User getUser(String login) throws NotSignedInException, IOException {
    UserService userService = getUserService();
    return userService.getUser(login);
  }

}
