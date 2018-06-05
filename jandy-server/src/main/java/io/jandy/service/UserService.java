package io.jandy.service;

import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.exception.UserNotFoundException;
import io.jandy.util.api.json.GHUser;
import io.jandy.util.api.GitHubApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author user1
 * @since 2015-06-30
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private GitHubApi gitHubApi;

  @Transactional
  private User getUser(GHUser ghUser) throws IOException {
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

  public User getUser(String login) throws IOException, UserNotFoundException {
    GHUser ghUser = gitHubApi.getUser(login);
    if (ghUser == null)
      throw new UserNotFoundException();
    return getUser(ghUser);
  }

  public User getSelf() throws IOException {
    GHUser self = gitHubApi.getUser();
    return self != null ? getUser(self) : null;
  }

  public boolean isAnonymous() {
    return gitHubApi.isAnonymous();
  }
}
