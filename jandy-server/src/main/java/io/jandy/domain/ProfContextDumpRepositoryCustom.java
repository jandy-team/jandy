package io.jandy.domain;

import java.util.List;

/**
 * @author JCooky
 * @since 2016-02-07
 */
public interface ProfContextDumpRepositoryCustom {
  ProfContextDump findPrev(ProfContextDump current);

  List<ProfContextDump> findByBuild(Build build);
}
