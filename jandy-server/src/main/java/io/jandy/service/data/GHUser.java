package io.jandy.service.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-08
 */
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

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public GHUser setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
    return this;
  }

  public String getLogin() {
    return login;
  }

  public GHUser setLogin(String login) {
    this.login = login;
    return this;
  }

  public Long getId() {
    return id;
  }

  public GHUser setId(Long id) {
    this.id = id;
    return this;
  }

  public Integer getPublicRepos() {
    return publicRepos;
  }

  public void setPublicRepos(Integer publicRepos) {
    this.publicRepos = publicRepos;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
