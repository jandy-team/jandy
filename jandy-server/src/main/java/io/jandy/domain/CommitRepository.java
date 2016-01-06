package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JCooky
 * @since 2016-01-06
 */
public interface CommitRepository extends JpaRepository<Commit, Long> {
}
