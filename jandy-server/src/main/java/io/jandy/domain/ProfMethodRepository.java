package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface ProfMethodRepository extends JpaRepository<ProfMethod, String> {
  ProfMethod findByNameAndDescriptorAndOwner_Id(String methodName, String signature, String id);
}
