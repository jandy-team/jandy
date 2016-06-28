package io.jandy.domain.data;

import java.io.Serializable;

/**
 * @author jcooky
 */
public class ProfilingInfo implements Serializable {
  private long buildId;
  private String sampleName;

  public long getBuildId() {
    return buildId;
  }

  public ProfilingInfo setBuildId(long buildId) {
    this.buildId = buildId;
    return this;
  }

  public String getSampleName() {
    return sampleName;
  }

  public ProfilingInfo setSampleName(String sampleName) {
    this.sampleName = sampleName;
    return this;
  }
}
