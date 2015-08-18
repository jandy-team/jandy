package io.jandy.java.metrics;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class ProfilingMetrics implements Externalizable {
  private TreeNode root;

  public ProfilingMetrics() {

  }

  public ProfilingMetrics(List<TreeNode> roots) {
    this.root = new TreeNode();
    for (TreeNode n : roots) {
      if (!n.getChildren().isEmpty()) {
        this.root.getChildren().add(n.getChildren().get(0));
      }
    }
  }

  public TreeNode getRoot() {
    return root;
  }

  public void setRoot(TreeNode root) {
    this.root = root;
  }

  public void writeExternal(ObjectOutput objectOutput) throws IOException {
    objectOutput.writeObject(root);
  }

  public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
    root = (TreeNode)objectInput.readObject();
  }
}
