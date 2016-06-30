package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2016-06-30
 */
@Entity
public class ProfThread {
  @Id
  @GeneratedValue
  private long id;

  private long threadId;
  private String threadName;

  @OneToOne
  @JsonIgnore
  private ProfTreeNode root;

  @ManyToOne
  @JsonIgnore
  private ProfContextDump profContext;

  public long getId() {
    return id;
  }

  public ProfThread setId(long id) {
    this.id = id;
    return this;
  }

  public long getThreadId() {
    return threadId;
  }

  public ProfThread setThreadId(long threadId) {
    this.threadId = threadId;
    return this;
  }

  public String getThreadName() {
    return threadName;
  }

  public ProfThread setThreadName(String threadName) {
    this.threadName = threadName;
    return this;
  }

  public ProfTreeNode getRoot() {
    return root;
  }

  public ProfThread setRoot(ProfTreeNode root) {
    this.root = root;
    return this;
  }

  public ProfContextDump getProfContext() {
    return profContext;
  }

  public ProfThread setProfContext(ProfContextDump profContext) {
    this.profContext = profContext;
    return this;
  }
}
