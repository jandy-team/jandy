package io.jandy.service.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-08
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GHOrg implements Serializable {
  @JsonProperty("login")
  private String login;

  @JsonProperty("id")
  private Long id;

  @JsonProperty("public_repos")
  private Integer publicRepos;

  public String getLogin() {
    return login;
  }

  public GHOrg setLogin(String login) {
    this.login = login;
    return this;
  }

  public Long getId() {
    return id;
  }

  public GHOrg setId(Long id) {
    this.id = id;
    return this;
  }

  public Integer getPublicRepos() {
    return publicRepos;
  }

  public GHOrg setPublicRepos(Integer publicRepos) {
    this.publicRepos = publicRepos;
    return this;
  }
}
