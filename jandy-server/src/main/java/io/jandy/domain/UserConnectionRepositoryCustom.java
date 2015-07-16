package io.jandy.domain;

import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public interface UserConnectionRepositoryCustom {
  List<UserConnection> findByUserIdAndProviderUsers(String userId, Map<String, List<String>> providerUsers);
  int getIncrementRank(String userId, String providerId);
}
