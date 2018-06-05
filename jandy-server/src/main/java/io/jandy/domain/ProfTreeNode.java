package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.experimental.Accessors;
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
@Data
@Accessors(chain = true)
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
}
