package io.jandy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public interface BuildRepository extends JpaRepository<Build, Long> {
  Build findByTravisBuildId(long travisBuildId);
  List<Build> findByBranch_Project_Id(long projectId);
  Build findByNumberAndBranch_NameAndBranch_Project_AccountAndBranch_Project_Name(long number, String branchName, String account, String projectName);
}
