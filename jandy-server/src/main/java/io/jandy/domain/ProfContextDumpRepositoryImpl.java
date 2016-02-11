package io.jandy.domain;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    Tuple tuple = qf.query()
        .from(prof)
        .join(prof.build).join(prof.sample)
        .where(prof.build.branch.id.eq(branchId))
        .groupBy(prof.sample.name)
        .having(prof.sample.name.eq(sampleName))
        .uniqueResult(prof.build.number.max(), prof);

    return tuple == null ? null : tuple.get(1, ProfContextDump.class);
  }

}
