package io.jandy.domain;

import com.mysema.query.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jandy.domain.QBranch.branch;

/**
 * @author jcooky
 */
public class ProjectRepositoryImpl implements ProjectRepositoryCustom {

  @Autowired
  private JPAQueryFactory qf;

  @Override
  public Project findByBuild(Build build) {
    QProject p = QProject.project;
    QBuild b = QBuild.build;
    QBranch br = branch;

    return qf.query()
        .from(p, b, br)
        .where(b.id.eq(build.getId()), b.branch.eq(br), br.project.eq(p))
        .uniqueResult(p);
  }
}
