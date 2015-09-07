package io.jandy.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
public class Project {
  @Id
  @GeneratedValue
  private long id;

  private String account;
  private String name;

  @Column(unique = true)
  private long gitHubId;

  @ManyToOne
  private User user;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "project")
  private List<Branch> branches = new ArrayList<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getGitHubId() {
    return gitHubId;
  }

  public void setGitHubId(long gitHubId) {
    this.gitHubId = gitHubId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<Branch> getBranches() {
    return branches;
  }

  public void setBranches(List<Branch> branches) {
    this.branches = branches;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Project{");
    sb.append("gitHubId=").append(gitHubId);
    sb.append(", name='").append(name).append('\'');
    sb.append(", account='").append(account).append('\'');
    sb.append(", id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
