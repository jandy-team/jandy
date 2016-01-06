package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
public class Build {
  @Id
  @GeneratedValue
  private long id;

  private String startedAt, finishedAt;
  private long duration;
  private String language;
  private long travisBuildId;
  private long number;
  private BuildState state = BuildState.PROGRESSING;

  @Transient
  private String buildAt;

  @OneToOne(cascade = CascadeType.REMOVE)
  private Commit commit;

  @ManyToOne
  @JsonIgnore
  private Branch branch;

  @OneToOne(mappedBy = "build", cascade = CascadeType.REMOVE)
  private ProfContextDump profContextDump;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public long getTravisBuildId() {
    return travisBuildId;
  }

  public void setTravisBuildId(long travisBuildId) {
    this.travisBuildId = travisBuildId;
  }

  public long getNumber() {
    return number;
  }

  public void setNumber(long number) {
    this.number = number;
  }

  public Branch getBranch() {
    return branch;
  }

  public void setBranch(Branch branch) {
    this.branch = branch;
  }

  public ProfContextDump getProfContextDump() {
    return profContextDump;
  }

  public void setProfContextDump(ProfContextDump profContextDump) {
    this.profContextDump = profContextDump;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Build{");
    sb.append("id=").append(id);
    sb.append(", language='").append(language).append('\'');
    sb.append(", travisBuildId=").append(travisBuildId);
    sb.append(", number=").append(number);
    sb.append('}');
    return sb.toString();
  }

  public void setCommit(Commit commit) {
    this.commit = commit;
  }

  public Commit getCommit() {
    return commit;
  }

  public BuildState getState() {
    return state;
  }

  public Build setState(BuildState state) {
    this.state = state;
    return this;
  }

  public String getStartedAt() {
    return startedAt;
  }

  public Build setStartedAt(String startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  public String getFinishedAt() {
    return finishedAt;
  }

  public Build setFinishedAt(String finishedAt) {
    this.finishedAt = finishedAt;
    return this;
  }

  public long getDuration() {
    return duration;
  }

  public Build setDuration(long duration) {
    this.duration = duration;
    return this;
  }

  public String getBuildAt() {
    return buildAt;
  }

  public Build setBuildAt(String buildAt) {
    this.buildAt = buildAt;
    return this;
  }
}
