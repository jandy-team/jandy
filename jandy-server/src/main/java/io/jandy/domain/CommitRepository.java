package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2016-01-06
 */
@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
}
