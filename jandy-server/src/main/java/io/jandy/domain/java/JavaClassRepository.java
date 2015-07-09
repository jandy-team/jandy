package io.jandy.domain.java;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface JavaClassRepository extends JpaRepository<JavaClass, Long> {
  JavaClass findByClassNameAndPackageName(String className, String packageName);
}
