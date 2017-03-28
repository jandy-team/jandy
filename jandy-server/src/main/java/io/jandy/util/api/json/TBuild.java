package io.jandy.util.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author JCooky
 * @since 2016-12-18
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TBuild {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("number")
  private Long number;

  @JsonProperty("started_at")
  private String startedAt;

  @JsonProperty("finished_at")
  private String finishedAt;

  @JsonProperty("state")
  private String state;

  @JsonProperty("commit_id")
  private Long commitId;

  @JsonProperty("duration")
  private Long duration;
}
