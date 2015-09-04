package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public interface UserConnectionRepository extends JpaRepository<UserConnection, UserConnectionId>, UserConnectionRepositoryCustom {
  List<UserConnection> findById_ProviderIdAndId_ProviderUserIdIn(String providerId, Collection<String> providerUserIds);

  List<UserConnection> findById_ProviderIdAndId_ProviderUserId(String providerId, String providerUserId);

  List<UserConnection> findById_UserIdOrderById_ProviderIdAscRankAsc(String userId);

  List<UserConnection> findById_UserIdAndId_ProviderIdOrderByRankAsc(String userId, String providerId);

}
