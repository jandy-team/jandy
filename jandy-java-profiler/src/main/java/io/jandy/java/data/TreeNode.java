package io.jandy.java.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class TreeNode {
  private String id;
  private boolean root;
  private List<String> childrenIds = new ArrayList<String>();
  private String methodId;
  private Accumulator acc;

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

  public List<String> getChildrenIds() {
    return childrenIds;
  }

  public void setChildrenIds(List<String> childrenIds) {
    this.childrenIds = childrenIds;
  }

  public void setMethodId(String methodId) {
    this.methodId = methodId;
  }

  public String getMethodId() {
    return methodId;
  }

  public void setAcc(Accumulator acc) {
    this.acc = acc;
  }

  public Accumulator getAcc() {
    return acc;
  }
}
