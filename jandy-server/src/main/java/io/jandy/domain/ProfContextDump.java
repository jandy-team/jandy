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
public class ProfContextDump implements Iterable<ProfTreeNode> {
  @Id
  @GeneratedValue
  private long id;

  private long maxTotalDuration;
  // elapsed duration to be compare prev build
  private Long elapsedDuration;

  @ElementCollection
  @CollectionTable
  private List<ProfTreeNode> slowedNodes;

  @OneToOne
  @JsonIgnore
  private ProfTreeNode root;

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

  public ProfTreeNode getRoot() {
    return root;
  }

  public ProfContextDump setRoot(ProfTreeNode root) {
    this.root = root;
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

  @Override
  @Transactional(readOnly = true)
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

  @Transactional(readOnly = true)
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
}
