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
public class TCommit {
  @JsonProperty("message")
  private String message;

  @JsonProperty("committed_at")
  private String committedAt;

  @JsonProperty("committer_name")
  private String committerName;

  @JsonProperty("committer_email")
  private String committerEmail;

  @JsonProperty("sha")
  private String sha;

  @JsonProperty("id")
  private Long id;
}
