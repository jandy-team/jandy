package io.jandy.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author JCooky
 * @since 2016-02-07
 */
public class ProfContextDumpRepositoryImpl implements ProfContextDumpRepositoryCustom {
  @Autowired
  private JPAQueryFactory qf;

  @Override
  public ProfContextDump findPrev(ProfContextDump current) {
    QSample s = QSample.sample;
    QBuild b = QBuild.build;
    QProfContextDump p = QProfContextDump.profContextDump;
    QBranch br = QBranch.branch;

    long branchId = current.getBuild().getBranch().getId();
    long buildNumber = current.getBuild().getNumber();
    long sampleId = current.getSample().getId();

    return qf.select(p)
        .from(s, b, br, p)
        .where(s.id.eq(sampleId), br.id.eq(branchId), s.builds.contains(b), b.samples.contains(s), p.build.eq(b), b.branch.eq(br), p.sample.eq(s),
            b.number.lt(buildNumber))
        .orderBy(b.number.desc())
        .limit(1L)
        .fetchOne();
  }

  @Override
  public List<ProfContextDump> findByBuild(Build build) {
    QBuild b = QBuild.build;
    QProfContextDump p = QProfContextDump.profContextDump;
    QSample s = QSample.sample;

    return qf.select(p)
        .from(s, b, p)
        .where(b.id.eq(build.getId()), b.samples.contains(s), p.build.eq(b), p.sample.eq(s))
        .fetch();
  }
}
