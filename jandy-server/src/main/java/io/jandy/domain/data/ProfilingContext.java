package io.jandy.domain.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class ProfilingContext {
  private List<TreeNode> treeNodes = new ArrayList<>();

  public List<TreeNode> getTreeNodes() {
    return treeNodes;
  }

  public ProfilingContext setTreeNodes(List<TreeNode> treeNodes) {
    this.treeNodes = treeNodes;
    return this;
  }
}
