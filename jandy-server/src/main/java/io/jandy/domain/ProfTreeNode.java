package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class ProfTreeNode {
  @Id
  @GeneratedValue
  private long id;

  private long elapsedTime;
  private long startTime;
  private String concurThreadName;

  @Transient
  private double offset;
  @Transient
  private double width;
  @Transient
  private int depth;

  @ManyToOne
  private ProfMethod method;

  @ManyToOne
  @JsonIgnore
  private ProfTreeNode parent;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
  private List<ProfTreeNode> children = new ArrayList<ProfTreeNode>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
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

  public void setMethod(ProfMethod method) {
    this.method = method;
  }

  public List<ProfTreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<ProfTreeNode> children) {
    this.children = children;
  }

  public double getOffset() {
    return offset;
  }

  public void setOffset(double offset) {
    this.offset = offset;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
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

  public String getConcurThreadName() {
    return concurThreadName;
  }

  public void setConcurThreadName(String concurThreadName) {
    this.concurThreadName = concurThreadName;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("method", method)
        .append("depth", depth)
        .append("width", width)
        .append("offset", offset)
        .append("concurThreadName", concurThreadName)
        .append("startTime", startTime)
        .append("elapsedTime", elapsedTime)
        .toString();
  }

}
