package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Repository
public interface ProfClassRepository extends JpaRepository<ProfClass, Long> {
  ProfClass findByNameAndPackageName(String name, String packageName);
}
