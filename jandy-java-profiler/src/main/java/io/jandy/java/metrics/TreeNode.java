package io.jandy.java.metrics;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class TreeNode implements Externalizable {
  private TreeNode parent;
  private List<TreeNode> children = new ArrayList<TreeNode>();
  private Accumulator accumulator = new Accumulator();
  private MethodKey methodKey;

  public TreeNode getParent() {
    return parent;
  }

  public void setParent(TreeNode parent) {
    this.parent = parent;
  }

  public List<TreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<TreeNode> children) {
    this.children = children;
  }

  public Accumulator getAccumulator() {
    return accumulator;
  }

  public void setAccumulator(Accumulator accumulator) {
    this.accumulator = accumulator;
  }

  public MethodKey getMethodKey() {
    return methodKey;
  }

  public void setMethodKey(MethodKey methodKey) {
    this.methodKey = methodKey;
  }

  public void writeExternal(ObjectOutput o) throws IOException {
    o.writeObject(parent);
    o.writeObject(children);
    o.writeObject(accumulator);
    o.writeObject(methodKey);
  }

  public void readExternal(ObjectInput i) throws IOException, ClassNotFoundException {
    this.parent = (TreeNode)i.readObject();
    this.children = (List<TreeNode>)i.readObject();
    this.accumulator = (Accumulator)i.readObject();
    this.methodKey = (MethodKey)i.readObject();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("TreeNode{");
    sb.append("children=").append(children);
    sb.append(", accumulator=").append(accumulator);
    sb.append(", methodKey=").append(methodKey);
    sb.append('}');
    return sb.toString();
  }
}
