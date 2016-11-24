package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Repository
public interface ProfContextDumpRepository extends ProfContextDumpRepositoryCustom, JpaRepository<ProfContextDump, Long>, QueryDslPredicateExecutor<ProfContextDump> {
}
