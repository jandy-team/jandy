package io.jandy.domain.java;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  private long totalEnters = 0;
  private long totalExits = 0;
  private long totalErrors = 0;
  private long minDuration = Long.MAX_VALUE;
  private long maxDuration = Long.MIN_VALUE;
  private long totalDuration = 0;    // used for mean
  private long sumOfSquares = 0;    // used for std dev
  private int concurrentThreads = 0;
  private int maxConcurrentThreads = 0;
  private long duration = 0;

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

  public long getTotalEnters() {
    return totalEnters;
  }

  public void setTotalEnters(long totalEnters) {
    this.totalEnters = totalEnters;
  }

  public long getTotalExits() {
    return totalExits;
  }

  public void setTotalExits(long totalExits) {
    this.totalExits = totalExits;
  }

  public long getTotalErrors() {
    return totalErrors;
  }

  public void setTotalErrors(long totalErrors) {
    this.totalErrors = totalErrors;
  }

  public long getMinDuration() {
    return minDuration;
  }

  public void setMinDuration(long minDuration) {
    this.minDuration = minDuration;
  }

  public long getMaxDuration() {
    return maxDuration;
  }

  public void setMaxDuration(long maxDuration) {
    this.maxDuration = maxDuration;
  }

  public long getTotalDuration() {
    return totalDuration;
  }

  public void setTotalDuration(long totalDuration) {
    this.totalDuration = totalDuration;
  }

  public long getSumOfSquares() {
    return sumOfSquares;
  }

  public void setSumOfSquares(long sumOfSquares) {
    this.sumOfSquares = sumOfSquares;
  }

  public int getConcurrentThreads() {
    return concurrentThreads;
  }

  public void setConcurrentThreads(int concurrentThreads) {
    this.concurrentThreads = concurrentThreads;
  }

  public int getMaxConcurrentThreads() {
    return maxConcurrentThreads;
  }

  public void setMaxConcurrentThreads(int maxConcurrentThreads) {
    this.maxConcurrentThreads = maxConcurrentThreads;
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

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("totalEnters", totalEnters)
        .append("totalExits", totalExits)
        .append("totalErrors", totalErrors)
        .append("minDuration", minDuration)
        .append("maxDuration", maxDuration)
        .append("totalDuration", totalDuration)
        .append("sumOfSquares", sumOfSquares)
        .append("concurrentThreads", concurrentThreads)
        .append("maxConcurrentThreads", maxConcurrentThreads)
        .append("javaMethod", javaMethod)
        .toString();
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

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }
}
