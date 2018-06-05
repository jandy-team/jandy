package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2016-06-30
 */
@Repository
public interface ProfThreadRepository extends JpaRepository<ProfThread, Long> {
}
