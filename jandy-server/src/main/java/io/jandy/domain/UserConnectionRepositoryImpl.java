package io.jandy.domain;

import com.google.common.collect.Iterables;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-07-16
 */
public class UserConnectionRepositoryImpl implements UserConnectionRepositoryCustom {
  @Autowired
  private EntityManager em;

  @Override
  public List<UserConnection> findByUserIdAndProviderUsers(String userId, Map<String, List<String>> providerUsers) {
    QUserConnection uc = QUserConnection.userConnection;
    BooleanExpression[] expressions = new BooleanExpression[providerUsers.size()];
    int i = 0;
    for (Map.Entry<String, List<String>> entry : providerUsers.entrySet()) {
      String providerId = entry.getKey();

      expressions[i++] = uc.id.providerId.eq(providerId).and(uc.id.providerUserId.in(entry.getValue()));
    }
    return new JPAQueryFactory(em).selectFrom(uc)
        .where(uc.id.userId.eq(userId), Expressions.anyOf(expressions))
        .orderBy(uc.id.providerId.asc(), uc.rank.asc())
        .fetch();
  }

  @Override
  public int getIncrementRank(String userId, String providerId) {
    QUserConnection uc = QUserConnection.userConnection;
    List<Integer> results = new JPAQueryFactory(em).from(uc)
        .where(uc.id.userId.eq(userId), uc.id.providerId.eq(providerId))
        .select(uc.rank.max().add(1).coalesce(Expressions.ONE).as("rank"))
        .fetch();

    return results.get(0);
  }
}
