package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Repository
public interface ProfMethodRepository extends JpaRepository<ProfMethod, Long> {
  ProfMethod findByNameAndDescriptorAndAccessAndOwner_Id(String methodName, String signature, int access, Long id);
}
