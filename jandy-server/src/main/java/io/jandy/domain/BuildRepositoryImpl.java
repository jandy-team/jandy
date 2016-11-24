package io.jandy.domain;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author JCooky
 * @since 2016-11-19
 */
public class BuildRepositoryImpl implements BuildRepositoryCustom {
  @Autowired
  private JPAQueryFactory queryFactory;

  @Override
  public long deleteProfilesById(long id) {
    QProfContextDump p = QProfContextDump.profContextDump;

    return queryFactory.delete(p)
        .where(p.build.id.eq(id))
        .execute();
  }
}
