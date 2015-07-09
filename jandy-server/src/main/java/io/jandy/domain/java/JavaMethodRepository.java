package io.jandy.domain.java;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface JavaMethodRepository extends JpaRepository<JavaMethod, Long> {
  JavaMethod findByMethodNameAndSignatureAndJavaClass_Id(String methodName, String signature, long id);
}
