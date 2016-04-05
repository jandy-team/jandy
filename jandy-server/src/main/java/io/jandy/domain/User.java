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
  private long gitHubId;

  private String avatarUrl;
  private String login;
  private int publicRepos;
  private String email;

  @Transient
  private String color;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
  private List<Project> projects = new ArrayList<>();


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

  public long getGitHubId() {
    return gitHubId;
  }

  public void setGitHubId(long gitHubId) {
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

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }
}
