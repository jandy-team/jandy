package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class ProfContextDump implements Iterable<ProfTreeNode> {
  @Id
  @GeneratedValue
  private long id;

  private long maxTotalDuration;

  @ElementCollection
  @CollectionTable
  private List<ProfTreeNode> slowedNodes;

  @OneToOne
  private ProfTreeNode root;

  @OneToOne
  @JsonIgnore
  private Build build;

  public long getId() {
    return id;
  }

  public ProfContextDump setId(long id) {
    this.id = id;
    return this;
  }

  public ProfTreeNode getRoot() {
    return root;
  }

  public ProfContextDump setRoot(ProfTreeNode root) {
    this.root = root;
    return this;
  }

  public ProfContextDump setBuild(Build build) {
    this.build = build;
    return this;
  }

  public Build getBuild() {
    return build;
  }

  public long getMaxTotalDuration() {
    return maxTotalDuration;
  }

  public ProfContextDump setMaxTotalDuration(long maxTotalDuration) {
    this.maxTotalDuration = maxTotalDuration;
    return this;
  }

  @Override
  public Iterator<ProfTreeNode> iterator() {
    return new JavaTreeNodeIterator(root);
  }

  public List<ProfTreeNode> getSlowedNodes() {
    return slowedNodes;
  }

  public ProfContextDump setSlowedNodes(List<ProfTreeNode> slowedNodes) {
    this.slowedNodes = slowedNodes;
    return this;
  }

  private class JavaTreeNodeIterator implements Iterator<ProfTreeNode> {

    private Deque<ProfTreeNode> nodes = new LinkedList<>();

    public JavaTreeNodeIterator(ProfTreeNode root) {
      this.nodes.addAll(Lists.reverse(root.getChildren()));
    }

    @Override
    public boolean hasNext() {
      return !nodes.isEmpty();
    }

    @Override
    public ProfTreeNode next() {
      ProfTreeNode node = nodes.pop();

      nodes.addAll(Lists.reverse(node.getChildren()));

      return node;
    }
  }
}
