package io.jandy.domain.data;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BuildInfo {
  private long buildId;
  private long buildNum;
  private String ownerName, repoName;
  private String branchName;
  private String lang;
  private int numSamples;
}