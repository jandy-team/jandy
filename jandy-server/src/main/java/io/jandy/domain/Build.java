package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jandy.domain.java.JavaProfilingDump;

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

  private String language;
  private long travisBuildId;
  private long number;

  @ManyToOne
  @JsonIgnore
  private Branch branch;

  @OneToOne(mappedBy = "build")
  private JavaProfilingDump javaProfilingDump;

  public long getId() {
    return id;
  }

  public Build setId(long id) {
    this.id = id;
    return this;
  }

  public String getLanguage() {
    return language;
  }

  public Build setLanguage(String language) {
    this.language = language;
    return this;
  }

  public long getTravisBuildId() {
    return travisBuildId;
  }

  public Build setTravisBuildId(long travisBuildId) {
    this.travisBuildId = travisBuildId;
    return this;
  }

  public long getNumber() {
    return number;
  }

  public Build setNumber(long number) {
    this.number = number;
    return this;
  }

  public Branch getBranch() {
    return branch;
  }

  public Build setBranch(Branch branch) {
    this.branch = branch;
    return this;
  }

  public JavaProfilingDump getJavaProfilingDump() {
    return javaProfilingDump;
  }

  public Build setJavaProfilingDump(JavaProfilingDump javaProfilingDump) {
    this.javaProfilingDump = javaProfilingDump;
    return this;
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
}
