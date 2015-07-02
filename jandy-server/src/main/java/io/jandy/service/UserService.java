package io.jandy.service;

import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.exception.NotSignedInException;
import io.jandy.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
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
  private GitHubService gitHubService;

  @Transactional
  public User getUser(org.eclipse.egit.github.core.User ghUser) throws IOException {
    User user = userRepository.findByGitHubId(ghUser.getId());
    if (user == null) {
      user = new User();
      user.setGitHubId(ghUser.getId());
      user = userRepository.save(user);
    }

    user.setAvatarUrl(ghUser.getAvatarUrl());
    user.setLogin(ghUser.getLogin());
    user.setPublicRepos(ghUser.getPublicRepos());

    return user;
  }

  public User getUser(String login) throws IOException, UserNotFoundException {
    try {
      return getUser(gitHubService.getUser(login));
    } catch (NotSignedInException e) {
      throw new UserNotFoundException(e);
    }
  }

  public User getSelf() throws IOException, UserNotFoundException {
    try {
      return getUser(gitHubService.getUser());
    } catch (NotSignedInException e) {
      throw new UserNotFoundException(e);
    }
  }
}
