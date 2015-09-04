package io.jandy.config.social;

import io.jandy.domain.UserConnection;
import io.jandy.domain.UserConnectionId;
import io.jandy.domain.UserConnectionRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public class JpaConnectionRepository implements ConnectionRepository {
  private UserConnectionRepository userConnectionRepository;
  private String userId;

  private final ServiceProviderConnectionMapper connectionMapper = new ServiceProviderConnectionMapper();
  private ConnectionFactoryLocator connectionFactoryLocator;
  private TextEncryptor textEncryptor;

  public JpaConnectionRepository(UserConnectionRepository userConnectionRepository, String userId, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
    this.userConnectionRepository = userConnectionRepository;
    this.userId = userId;
    this.connectionFactoryLocator = connectionFactoryLocator;
    this.textEncryptor = textEncryptor;
  }

  @Override
  public MultiValueMap<String, Connection<?>> findAllConnections() {
    List<Connection<?>> resultList = connectionMapper.mapConnections(userConnectionRepository.findById_UserIdOrderById_ProviderIdAscRankAsc(userId));
    MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
    Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
    for (String registeredProviderId : registeredProviderIds) {
      connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
    }
    for (Connection<?> connection : resultList) {
      String providerId = connection.getKey().getProviderId();
      if (connections.get(providerId).size() == 0) {
        connections.put(providerId, new LinkedList<Connection<?>>());
      }
      connections.add(providerId, connection);
    }
    return connections;
  }

  @Override
  public List<Connection<?>> findConnections(String providerId) {
    return connectionMapper.mapConnections(userConnectionRepository.findById_UserIdAndId_ProviderIdOrderByRankAsc(userId, providerId));
  }

  @Override
  public <A> List<Connection<A>> findConnections(Class<A> apiType) {
    List<?> connections = findConnections(getProviderId(apiType));

    return (List<Connection<A>>)connections;
  }

  @Override
  public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
    if (providerUsers == null || providerUsers.isEmpty()) {
      throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
    }

    List<Connection<?>> resultList = connectionMapper.mapConnections(userConnectionRepository.findByUserIdAndProviderUsers(userId, providerUsers));
    MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
    for (Connection<?> connection : resultList) {
      String providerId = connection.getKey().getProviderId();
      List<String> userIds = providerUsers.get(providerId);
      List<Connection<?>> connections = connectionsForUsers.get(providerId);
      if (connections == null) {
        connections = new ArrayList<Connection<?>>(userIds.size());
        for (int i = 0; i < userIds.size(); i++) {
          connections.add(null);
        }
        connectionsForUsers.put(providerId, connections);
      }
      String providerUserId = connection.getKey().getProviderUserId();
      int connectionIndex = userIds.indexOf(providerUserId);
      connections.set(connectionIndex, connection);
    }

    return connectionsForUsers;
  }

  @Override
  public Connection<?> getConnection(ConnectionKey connectionKey) {
    UserConnection uc = userConnectionRepository.findOne(new UserConnectionId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()));
    if (uc == null)
      throw new NoSuchConnectionException(connectionKey);

    return connectionMapper.mapConnection(uc);
  }

  @Override
  public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
    String providerId = getProviderId(apiType);
    return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
  }

  @Override
  public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
    String providerId = getProviderId(apiType);
    Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
    if (connection == null) {
      throw new NotConnectedException(providerId);
    }
    return connection;
  }

  @Override
  public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
    String providerId = getProviderId(apiType);
    return (Connection<A>) findPrimaryConnection(providerId);
  }

  @Override
  @Transactional
  public void addConnection(Connection<?> connection) {
    try {
      ConnectionData data = connection.createData();
      int rank = userConnectionRepository.getIncrementRank(userId, data.getProviderId());

      UserConnection uc = new UserConnection();
      uc.setId(new UserConnectionId(userId, data.getProviderId(), data.getProviderUserId()));
      uc.setRank(rank);
      uc.setDisplayName(data.getDisplayName());
      uc.setProfileUrl(data.getProfileUrl());
      uc.setImageUrl(data.getImageUrl());
      uc.setAccessToken(encrypt(data.getAccessToken()));
      uc.setSecret(encrypt(data.getSecret()));
      uc.setRefreshToken(encrypt(data.getRefreshToken()));
      uc.setExpireTime(data.getExpireTime());

      userConnectionRepository.save(uc);
    } catch (DuplicateKeyException e) {
      throw new DuplicateConnectionException(connection.getKey());
    }
  }

  @Override
  @Transactional
  public void updateConnection(Connection<?> connection) {
    ConnectionData data = connection.createData();
    UserConnection uc = userConnectionRepository.findOne(new UserConnectionId(userId, data.getProviderId(), data.getProviderUserId()));
    uc.setDisplayName(data.getDisplayName());
    uc.setProfileUrl(data.getProfileUrl());
    uc.setImageUrl(data.getImageUrl());
    uc.setAccessToken(encrypt(data.getAccessToken()));
    uc.setSecret(encrypt(data.getSecret()));
    uc.setRefreshToken(encrypt(data.getRefreshToken()));
    uc.setExpireTime(data.getExpireTime());

    userConnectionRepository.save(uc);
  }

  @Override
  @Transactional
  public void removeConnections(String providerId) {
    userConnectionRepository.deleteByUserIdAndProviderId(userId, providerId);
  }

  @Override
  @Transactional
  public void removeConnection(ConnectionKey connectionKey) {
    userConnectionRepository.delete(new UserConnectionId(userId, connectionKey.getProviderId(), connectionKey.getProviderUserId()));
  }

  private Connection<?> findPrimaryConnection(String providerId) {
    List<Connection<?>> connections = connectionMapper.mapConnections(userConnectionRepository.findById_UserIdAndId_ProviderIdOrderByRankAsc(userId, providerId));
    if (connections.size() > 0) {
      return connections.get(0);
    } else {
      return null;
    }
  }

  private class ServiceProviderConnectionMapper {
    public List<Connection<?>> mapConnections(List<UserConnection> userConnections) {
      return userConnections.stream().map(userConnection -> this.mapConnection(userConnection)).collect(Collectors.toList());
    }

    public Connection<?> mapConnection(UserConnection userConnection) {
      ConnectionData connectionData = mapConnectionData(userConnection);
      ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
      return connectionFactory.createConnection(connectionData);
    }

    private ConnectionData mapConnectionData(UserConnection userConnection) {
      return new ConnectionData(userConnection.getId().getProviderId(), userConnection.getId().getProviderUserId(), userConnection.getDisplayName(),
          userConnection.getProfileUrl(), userConnection.getImageUrl(), decrypt(userConnection.getAccessToken()), decrypt(userConnection.getSecret()),
          decrypt(userConnection.getRefreshToken()), userConnection.getExpireTime());
    }

    private String decrypt(String encryptedText) {
      return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
    }

    private Long expireTime(long expireTime) { return expireTime == 0 ? null : expireTime; }
  }

  private <A> String getProviderId(Class<A> apiType) {
    return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
  }

  private String encrypt(String text) {
    return text != null ? textEncryptor.encrypt(text) : text;
  }

}
