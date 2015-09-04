package io.jandy.domain.java;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
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

  public JavaTreeNode setId(long id) {
    this.id = id;
    return this;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public JavaTreeNode setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
    return this;
  }

  public long getStartTime() {
    return startTime;
  }

  public JavaTreeNode setStartTime(long startTime) {
    this.startTime = startTime;
    return this;
  }

  public String getConcurThreadName() {
    return concurThreadName;
  }

  public JavaTreeNode setConcurThreadName(String concurThreadName) {
    this.concurThreadName = concurThreadName;
    return this;
  }

  public double getOffset() {
    return offset;
  }

  public JavaTreeNode setOffset(double offset) {
    this.offset = offset;
    return this;
  }

  public double getWidth() {
    return width;
  }

  public JavaTreeNode setWidth(double width) {
    this.width = width;
    return this;
  }

  public int getDepth() {
    return depth;
  }

  public JavaTreeNode setDepth(int depth) {
    this.depth = depth;
    return this;
  }

  public JavaMethod getJavaMethod() {
    return javaMethod;
  }

  public JavaTreeNode setJavaMethod(JavaMethod javaMethod) {
    this.javaMethod = javaMethod;
    return this;
  }

  public JavaTreeNode getParent() {
    return parent;
  }

  public JavaTreeNode setParent(JavaTreeNode parent) {
    this.parent = parent;
    return this;
  }

  public List<JavaTreeNode> getChildren() {
    return children;
  }

  public JavaTreeNode setChildren(List<JavaTreeNode> children) {
    this.children = children;
    return this;
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
