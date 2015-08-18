package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.metrics.TreeNode;
import io.jandy.java.metrics.TreeNodeFactory;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MethodCollector {
  private TreeNode root = new TreeNode(), latest = root;
  private Deque<TreeNode> treeNodes = new LinkedList<TreeNode>();

  public void enter(ClassType owner, MethodType method, long startTime) {
    treeNodes.push(latest);
    latest = TreeNodeFactory.getTreeNode(owner, method);
    latest.getAccumulator().setStartTime(startTime);
    latest.getAccumulator().setConcurThreadName(Thread.currentThread().getName());
  }

  public void exit(ClassType owner, MethodType method, Throwable throwable, long elapsedTime) {
    assert(latest.getMethodKey().getName().equals(method.getName()));
    assert(latest.getMethodKey().getDescriptor().equals(method.getDescriptor()));
    assert(latest.getMethodKey().getAccess() == method.getAccess());
    assert(latest.getMethodKey().getOwner().getName().equals(owner.getName()));
    assert(latest.getMethodKey().getOwner().getPackageName().equals(owner.getPackageName()));

    latest.getAccumulator().setElapsedTime(elapsedTime);
    if (throwable != null)
      latest.getAccumulator().setExceptionKey(TreeNodeFactory.getExceptionKey(throwable));

    TreeNode parent = treeNodes.pop();
    latest.setParent(parent);
    parent.getChildren().add(latest);
    latest = parent;
  }

  public TreeNode getRoot() {
    return root;
  }
}
