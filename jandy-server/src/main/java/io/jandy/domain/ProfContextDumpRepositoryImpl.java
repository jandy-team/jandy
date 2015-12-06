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
  public ProfContextDump findLastProfile(long branchId, String sampleName) {
    QProfContextDump prof = QProfContextDump.profContextDump;
    QBuild b = QBuild.build;
    QSample s = QSample.sample;

    return qf.query()
        .from(prof)
        .where(prof.build.branch.id.eq(branchId), prof.sample.name.eq(sampleName), prof.build.number.eq(
            qf.subQuery()
                .from(b, s)
                .where(b.samples.contains(s), b.branch.id.eq(branchId), s.name.eq(sampleName))
                .unique(b.number.max())
        ))
        .uniqueResult(prof);
  }

}
