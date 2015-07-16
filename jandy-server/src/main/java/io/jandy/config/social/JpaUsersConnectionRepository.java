package io.jandy.config.social;

import io.jandy.domain.UserConnectionRepository;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public class JpaUsersConnectionRepository implements UsersConnectionRepository {
  private ConnectionSignUp connectionSignUp;
  private UserConnectionRepository userConnectionRepository;
  private ConnectionFactoryLocator connectionFactoryLocator;
  private TextEncryptor textEncryptor;

  public JpaUsersConnectionRepository(UserConnectionRepository userConnectionRepository, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
    this.userConnectionRepository = userConnectionRepository;
    this.connectionFactoryLocator = connectionFactoryLocator;
    this.textEncryptor = textEncryptor;
  }

  @Override
  public List<String> findUserIdsWithConnection(Connection<?> connection) {
    ConnectionKey key = connection.getKey();
    List<String> localUserIds = userConnectionRepository.findById_ProviderIdAndId_ProviderUserId(key.getProviderId(), key.getProviderUserId()).stream().map(userConnection -> userConnection.getId().getUserId()).collect(Collectors.toList());

    if (localUserIds.size() == 0 && connectionSignUp != null) {
      String newUserId = connectionSignUp.execute(connection);
      if (newUserId != null) {
        createConnectionRepository(newUserId).addConnection(connection);
        return Arrays.asList(newUserId);
      }
    }

    return localUserIds;
  }

  @Override
  public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {

    return userConnectionRepository.findById_ProviderIdAndId_ProviderUserIdIn(providerId, providerUserIds).stream().map(userConnection -> userConnection.getId().getUserId()).collect(Collectors.toSet());
  }

  @Override
  public ConnectionRepository createConnectionRepository(String userId) {
    if (userId == null) {
      throw new IllegalArgumentException("userId cannot be null");
    }

    return new JpaConnectionRepository(userConnectionRepository, userId, connectionFactoryLocator, textEncryptor);
  }

  public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
    this.connectionSignUp = connectionSignUp;
  }
}
