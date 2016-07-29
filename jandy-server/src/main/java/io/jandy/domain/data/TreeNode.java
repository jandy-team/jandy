package io.jandy.domain.data;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class TreeNode implements Serializable {
  private String id = null;
  private boolean root;
  private MethodObject method;
  private Accumulator acc;
  private String parentId;
  private long profId;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setRoot(boolean root) {
    this.root = root;
  }

  public boolean isRoot() {
    return root;
  }

  public void setAcc(Accumulator acc) {
    this.acc = acc;
  }

  public Accumulator getAcc() {
    return acc;
  }

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public MethodObject getMethod() {
    return method;
  }

  public TreeNode setMethod(MethodObject method) {
    this.method = method;
    return this;
  }

  public long getProfId() {
    return profId;
  }

  public void setProfId(long profId) {
    this.profId = profId;
  }
}
