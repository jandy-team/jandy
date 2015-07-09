package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-06
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
  Branch findByNameAndProject_AccountAndName(String name, String account, String projectName);
}
