package io.jandy.domain.java;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import io.jandy.domain.Build;

import javax.persistence.*;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class JavaProfilingDump implements Iterable<JavaTreeNode> {
  @Id
  @GeneratedValue
  private long id;

  private long maxTotalDuration;

  @OneToOne
  @JsonIgnore
  private JavaTreeNode root;

  @OneToOne
  @JsonIgnore
  private Build build;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public JavaTreeNode getRoot() {
    return root;
  }

  public void setRoot(JavaTreeNode root) {
    this.root = root;
  }

  public void setBuild(Build build) {
    this.build = build;
  }

  public Build getBuild() {
    return build;
  }

  public long getMaxTotalDuration() {
    return maxTotalDuration;
  }

  public void setMaxTotalDuration(long maxTotalDuration) {
    this.maxTotalDuration = maxTotalDuration;
  }

  @Override
  public Iterator<JavaTreeNode> iterator() {
    return new JavaTreeNodeIterator(root);
  }

  private class JavaTreeNodeIterator implements Iterator<JavaTreeNode> {

    private Deque<JavaTreeNode> nodes = new LinkedList<>();

    public JavaTreeNodeIterator(JavaTreeNode root) {
      this.nodes.addAll(Lists.reverse(root.getChildren()));
    }

    @Override
    public boolean hasNext() {
      return !nodes.isEmpty();
    }

    @Override
    public JavaTreeNode next() {
      JavaTreeNode node = nodes.pop();

      nodes.addAll(Lists.reverse(node.getChildren()));

      return node;
    }
  }
}
