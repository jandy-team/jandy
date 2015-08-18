package io.jandy.domain.java;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jandy.java.metrics.ExceptionKey;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class JavaTreeNode {
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
  private JavaMethod javaMethod;

  @ManyToOne
  @JsonIgnore
  private JavaTreeNode parent;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "parent")
  private List<JavaTreeNode> children = new ArrayList<JavaTreeNode>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public JavaTreeNode getParent() {
    return parent;
  }

  public void setParent(JavaTreeNode parent) {
    this.parent = parent;
  }

  public JavaMethod getJavaMethod() {
    return javaMethod;
  }

  public void setJavaMethod(JavaMethod javaMethod) {
    this.javaMethod = javaMethod;
  }

  public List<JavaTreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<JavaTreeNode> children) {
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
    final StringBuilder sb = new StringBuilder("JavaTreeNode{");
    sb.append("javaMethod=").append(javaMethod);
    sb.append(", depth=").append(depth);
    sb.append(", width=").append(width);
    sb.append(", offset=").append(offset);
    sb.append(", concurThreadName='").append(concurThreadName).append('\'');
    sb.append(", startTime=").append(startTime);
    sb.append(", elapsedTime=").append(elapsedTime);
    sb.append(", id=").append(id);
    sb.append('}');
    return sb.toString();
  }
}
