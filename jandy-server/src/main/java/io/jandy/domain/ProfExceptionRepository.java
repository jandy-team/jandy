package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2016-06-14
 */
@Repository
public interface ProfExceptionRepository extends JpaRepository<ProfException, String> {
}
