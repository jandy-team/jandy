package io.jandy.domain;

import com.google.common.collect.Iterables;
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
    return new JPAQuery(em).from(uc)
        .where(uc.id.userId.eq(userId), Expressions.anyOf(expressions))
        .orderBy(uc.id.providerId.asc(), uc.rank.asc())
        .list(uc);
  }

  @Override
  public int getIncrementRank(String userId, String providerId) {
    QUserConnection uc = QUserConnection.userConnection;
    Integer result = new JPAQuery(em).from(uc)
        .where(uc.id.userId.eq(userId), uc.id.providerId.eq(providerId))
        .singleResult(uc.rank.max().add(1).coalesce(1).as("rank"));


    return result;
  }
}
