package io.jandy.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-29
 */
@Entity
public class User {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private int gitHubId;

  @Transient
  private String avatarUrl;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user")
  private List<Project> projects = new ArrayList<>();

  @Transient
  private String login;
  @Transient
  private int publicRepos;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<Project> getProjects() {
    return projects;
  }

  public void setProjects(List<Project> projects) {
    this.projects = projects;
  }

  public int getGitHubId() {
    return gitHubId;
  }

  public void setGitHubId(int gitHubId) {
    this.gitHubId = gitHubId;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }


  public void setLogin(String login) {
    this.login = login;
  }

  public String getLogin() {
    return login;
  }

  public void setPublicRepos(int publicRepos) {
    this.publicRepos = publicRepos;
  }

  public int getPublicRepos() {
    return publicRepos;
  }
}
