package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class ProfContextDump {
  @Id
  @GeneratedValue
  private long id;

  private ProfContextState state;
  private long maxTotalDuration;
  // elapsed duration to be compare prev build
  private Long elapsedDuration;

  @ElementCollection
  @CollectionTable
  private List<ProfTreeNode> slowedNodes;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "profContext")
  @JsonIgnore
  private List<ProfThread> threads = new ArrayList<>();

  @ManyToOne
  @JsonIgnore
  private Sample sample;

  @ManyToOne
  @JsonIgnore
  private Build build;

  public long getId() {
    return id;
  }

  public ProfContextDump setId(long id) {
    this.id = id;
    return this;
  }

  public ProfContextDump setSample(Sample sample) {
    this.sample = sample;
    return this;
  }

  public Sample getSample() {
    return sample;
  }

  public long getMaxTotalDuration() {
    return maxTotalDuration;
  }

  public ProfContextDump setMaxTotalDuration(long maxTotalDuration) {
    this.maxTotalDuration = maxTotalDuration;
    return this;
  }

  public List<ProfTreeNode> getSlowedNodes() {
    return slowedNodes;
  }

  public ProfContextDump setSlowedNodes(List<ProfTreeNode> slowedNodes) {
    this.slowedNodes = slowedNodes;
    return this;
  }

  public void setElapsedDuration(Long elapsedDuration) {
    this.elapsedDuration = elapsedDuration;
  }

  public Long getElapsedDuration() {
    return elapsedDuration;
  }

  public Build getBuild() {
    return build;
  }

  public ProfContextDump setBuild(Build build) {
    this.build = build;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("maxTotalDuration", maxTotalDuration)
        .append("elapsedDuration", elapsedDuration)
        .append("slowedNodes", slowedNodes)
        .append("build_id", build.getId())
        .append("sample_id", sample.getId())
        .toString();
  }

  public List<ProfThread> getThreads() {
    return threads;
  }

  public ProfContextDump setThreads(List<ProfThread> threads) {
    this.threads = threads;
    return this;
  }

  public ProfContextState getState() {
    return state;
  }

  public ProfContextDump setState(ProfContextState state) {
    this.state = state;
    return this;
  }
}
