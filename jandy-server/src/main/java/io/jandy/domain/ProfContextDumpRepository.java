package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface ProfContextDumpRepository extends JpaRepository<ProfContextDump, Long>, QueryDslPredicateExecutor<ProfContextDump> {
}
