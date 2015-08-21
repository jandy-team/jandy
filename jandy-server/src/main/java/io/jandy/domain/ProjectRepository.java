package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2015-07-01
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, QueryDslPredicateExecutor<Project> {
  Project findByGitHubId(long gitHubId);

  Project findByAccountAndName(String account, String name);
}
