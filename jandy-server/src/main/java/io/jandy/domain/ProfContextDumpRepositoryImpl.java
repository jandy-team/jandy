package io.jandy.domain;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import io.jandy.JandyApplicationServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.stream.Collectors;

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

    return qf.query()
        .from(s, b, br, p)
        .where(s.id.eq(sampleId), br.id.eq(branchId), s.builds.contains(b), b.samples.contains(s), p.build.eq(b), b.branch.eq(br), p.sample.eq(s),
            b.number.lt(buildNumber))
        .orderBy(b.number.desc())
        .limit(1L)
        .uniqueResult(p);
  }

  @Override
  public List<ProfContextDump> findByBuild(Build build) {
    QBuild b = QBuild.build;
    QProfContextDump p = QProfContextDump.profContextDump;
    QSample s = QSample.sample;

    return qf.query()
        .from(s, b, p)
        .where(b.id.eq(build.getId()), b.samples.contains(s), p.build.eq(b), p.sample.eq(s))
        .list(p);
  }
}
