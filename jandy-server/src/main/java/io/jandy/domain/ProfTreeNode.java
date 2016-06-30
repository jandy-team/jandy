package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class ProfTreeNode {
  @Id
  private String id;

  private long elapsedTime;
  private long startTime;
  private boolean root;

  @OneToOne
  private ProfException exception;

  @ManyToOne
  private ProfMethod method;

  @ManyToOne
  @JsonIgnore
  @Expose
  private ProfTreeNode parent;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
  private List<ProfTreeNode> children = new ArrayList<ProfTreeNode>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ProfTreeNode getParent() {
    return parent;
  }

  public void setParent(ProfTreeNode parent) {
    this.parent = parent;
  }

  public ProfMethod getMethod() {
    return method;
  }

  public String getParentId() { return this.parent == null ? null : this.parent.id; }

  public void setMethod(ProfMethod method) {
    this.method = method;
  }

  public List<ProfTreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<ProfTreeNode> children) {
    this.children = children;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("method", method)
        .append("startTime", startTime)
        .append("elapsedTime", elapsedTime)
        .append("parent", parent == null ? "" : parent.getId())
        .append("children", children.stream().map(ProfTreeNode::getId).collect(Collectors.toList()))
        .toString();
  }

  public boolean isRoot() {
    return root;
  }

  public ProfTreeNode setRoot(boolean root) {
    this.root = root;
    return this;
  }

  public ProfException getException() {
    return exception;
  }

  public ProfTreeNode setException(ProfException exception) {
    this.exception = exception;
    return this;
  }
}
