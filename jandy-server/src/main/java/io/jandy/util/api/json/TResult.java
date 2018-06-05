package io.jandy.util.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author JCooky
 * @since 2016-12-19
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TResult {
  @JsonProperty("build")
  private TBuild build;

  @JsonProperty("commit")
  private TCommit commit;
}
