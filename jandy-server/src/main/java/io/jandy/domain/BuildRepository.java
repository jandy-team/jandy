package io.jandy.domain;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Repository
public interface BuildRepository extends JpaRepository<Build, Long>, QueryDslPredicateExecutor<Build>, BuildRepositoryCustom {
  Build findByTravisBuildId(long travisBuildId);
  List<Build> findByBranch_Project_Id(long projectId, Sort sort);
  Build findByNumberAndBranch_NameAndBranch_Project_AccountAndBranch_Project_Name(long number, String branchName, String account, String projectName);

  Build findByNumberAndBranch_Id(long number, long id);

}
