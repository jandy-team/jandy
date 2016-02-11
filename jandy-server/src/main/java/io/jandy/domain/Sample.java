package io.jandy.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-02-06
 */
@Entity
public class Sample {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne
  private Project project;

  @ManyToMany
  @OrderBy("number DESC")
  private List<Build> builds = new ArrayList<>();

  @OneToMany(mappedBy = "sample", cascade = CascadeType.REMOVE)
  private List<ProfContextDump> profiles = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public Sample setId(Long id) {
    this.id = id;
    return this;
  }

  public List<ProfContextDump> getProfiles() {
    return profiles;
  }

  public Sample setProfiles(List<ProfContextDump> profiles) {
    this.profiles = profiles;
    return this;
  }

  public List<Build> getBuilds() {
    return builds;
  }

  public Sample setBuilds(List<Build> builds) {
    this.builds = builds;
    return this;
  }

  public Sample setName(String name) {
    this.name = name;
    return this;
  }

  public String getName() {
    return name;
  }

  public Sample setProject(Project project) {
    this.project = project;
    return this;
  }

  public Project getProject() {
    return project;
  }
}
