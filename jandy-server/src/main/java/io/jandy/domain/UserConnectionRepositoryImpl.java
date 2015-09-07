package io.jandy.domain;

import com.google.common.collect.Iterables;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public class UserConnectionRepositoryImpl implements UserConnectionRepositoryCustom {
  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public List<UserConnection> findByUserIdAndProviderUsers(String userId, Map<String, List<String>> providerUsers) {
    QUserConnection uc = QUserConnection.userConnection;

    return queryFactory.from(uc)
        .where(uc.id.userId.eq(userId), providerUsers.entrySet().stream()
            .map((e) -> uc.id.providerId.eq(e.getKey()).and(uc.id.providerUserId.in(e.getValue())))
            .reduce(BooleanExpression::or)
            .get())
        .orderBy(uc.id.providerId.asc(), uc.rank.asc())
        .list(uc);
  }

  @Override
  public int getIncrementRank(String userId, String providerId) {
    QUserConnection uc = QUserConnection.userConnection;
    return queryFactory.from(uc)
        .where(uc.id.userId.eq(userId), uc.id.providerId.eq(providerId))
        .singleResult(uc.rank.max().add(1).coalesce(1).as("rank"));
  }

  public long deleteByUserIdAndProviderId(String userId, String providerId) {
    QUserConnection uc = QUserConnection.userConnection;

    return queryFactory.delete(uc)
        .where(uc.id.userId.eq(userId), uc.id.providerId.eq(providerId))
        .execute();
  }
}
