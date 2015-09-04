package io.jandy.service;

import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.exception.NotSignedInException;
import io.jandy.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.cert.Extension;

/**
 * @author user1
 * @since 2015-06-30
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private GitHubService gitHubService;

  @Transactional
  public User getUser(org.eclipse.egit.github.core.User ghUser) throws IOException {
    User user = userRepository.findByGitHubId(ghUser.getId());
    if (user == null) {
      user = new User();
      user.setGitHubId(ghUser.getId());
    }

    user.setAvatarUrl(ghUser.getAvatarUrl());
    user.setLogin(ghUser.getLogin());
    user.setPublicRepos(ghUser.getPublicRepos());
    user.setEmail(ghUser.getEmail());
    return userRepository.save(user);
  }

  public User getUser(long id) {
    return userRepository.findOne(id);
  }

  public User getUser(String login) throws IOException, UserNotFoundException {
    try {
      return getUser(gitHubService.getUser(login));
    } catch (NotSignedInException e) {
      throw new UserNotFoundException(e);
    }
  }

  public User getSelf() throws IOException, NotSignedInException {
    return getUser(gitHubService.getUser());
  }

  @Transactional
  public User signIn(long userId, Connection<? extends GitHub> connection) {
    GitHubUserProfile profile = connection.getApi().userOperations().getUserProfile();
    User user = userRepository.findOne(userId);
    user.setLogin(profile.getLogin());
    user.setGitHubId(profile.getId());
    user.setAvatarUrl(profile.getAvatarUrl());

    return userRepository.save(user);
  }

  @Transactional
  public User signUp(Connection<? extends GitHub> connection) {
    GitHubUserProfile profile = connection.getApi().userOperations().getUserProfile();
    User user = new User();
    user.setLogin(profile.getLogin());
    user.setGitHubId(profile.getId());
    user.setAvatarUrl(profile.getAvatarUrl());

    return userRepository.save(user);
  }
}
