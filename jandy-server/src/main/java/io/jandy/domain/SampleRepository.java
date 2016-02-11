package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author JCooky
 * @since 2016-02-07
 */
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
  Sample findByNameAndProject_Id(String name, long projectId);
}
