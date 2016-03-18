package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface ProfClassRepository extends JpaRepository<ProfClass, String> {
  ProfClass findByNameAndPackageName(String name, String packageName);
}
