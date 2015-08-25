package io.jandy.domain.java;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface JavaProfilingDumpRepository extends JpaRepository<JavaProfilingDump, Long>, QueryDslPredicateExecutor<JavaProfilingDump> {
}
