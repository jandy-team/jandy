package io.jandy.util.api.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-08
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHUser implements Serializable {
  @JsonProperty("id")
  private long id;

  @JsonProperty("login")
  private String login;

  @JsonProperty("avatar_url")
  private String avatarUrl;

  @JsonProperty("public_repos")
  private Integer publicRepos;

  private String email;
}
