package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
public class Branch {
  @Id
  @GeneratedValue
  private long id;

  private String name;

  @ManyToOne
  @JsonIgnore
  private Project project;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "branch")
  private List<Build> builds = new ArrayList<>();

  public long getId() {
    return id;
  }

  public Branch setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Branch setName(String name) {
    this.name = name;
    return this;
  }

  public Project getProject() {
    return project;
  }

  public Branch setProject(Project project) {
    this.project = project;
    return this;
  }

  public List<Build> getBuilds() {
    return builds;
  }

  public Branch setBuilds(List<Build> builds) {
    this.builds = builds;
    return this;
  }
}
