package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.metrics.MetricsFactory;
import io.jandy.thrift.java.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MethodHandler {
  private TreeNode root = new TreeNode(), latest = root;
  private Deque<TreeNode> treeNodes = new LinkedList<TreeNode>();

  public void enter(ClassType owner, MethodType method) {
    treeNodes.push(latest);
    latest = MetricsFactory.getTreeNode(owner, method);
    latest.acc.setConcurThreadName(Thread.currentThread().getName());
  }

  public void exit(ClassType owner, MethodType method, long startTime, Throwable throwable, long elapsedTime) {
    assert(latest.method.getName().equals(method.getName()));
    assert(latest.method.getDescriptor().equals(method.getDescriptor()));
    assert(latest.method.getAccess() == method.getAccess());
    assert(latest.method.getOwner().getName().equals(owner.getName()));
    assert(latest.method.getOwner().getPackageName().equals(owner.getPackageName()));

    latest.acc.setStartTime(startTime);
    latest.acc.setElapsedTime(elapsedTime);
    if (throwable != null)
      latest.acc.setExceptionKey(MetricsFactory.getExceptionKey(throwable));

    TreeNode parent = treeNodes.pop();
    parent.addToChildren(latest);
    latest = parent;
  }

  public TreeNode getRoot() {
    return root;
  }
}
