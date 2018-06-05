package io.jandy.util.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GHOrg implements Serializable {
  @JsonProperty("login")
  private String login;

  @JsonProperty("id")
  private Long id;

  @JsonProperty("public_repos")
  private Integer publicRepos;
}
