package io.jandy.domain;

/**
 * @author JCooky
 * @since 2016-02-07
 */
public interface ProfContextDumpRepositoryCustom {
  ProfContextDump findLastProfile(long branchId, String sampleName);
}
