package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.JavaProfilingContext;
import io.jandy.java.data.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MethodHandler {
  private final JavaProfilingContext context;
  private TreeNode root = new TreeNode(), latest = root;
  private Deque<TreeNode> treeNodes = new LinkedList<TreeNode>();

  public MethodHandler(JavaProfilingContext context) {
    this.context = context;
  }

  public void enter(ClassType owner, MethodType method) {
    treeNodes.push(latest);
    latest = this.context.getTreeNode(owner, method);
    latest.getAcc().setConcurThreadName(Thread.currentThread().getName());
  }

  public void exit(ClassType owner, MethodType method, long startTime, Throwable throwable, long elapsedTime) {
//    assert(latest.method.getName().equals(method.getName()));
//    assert(latest.method.getDescriptor().equals(method.getDescriptor()));
//    assert(latest.method.getAccess() == method.getAccess());
//    assert(latest.method.getOwner().getName().equals(owner.getName()));
//    assert(latest.method.getOwner().getPackageName().equals(owner.getPackageName()));

    latest.getAcc().setStartTime(startTime);
    latest.getAcc().setElapsedTime(elapsedTime);
    if (throwable != null)
      latest.getAcc().setExceptionId(context.getExceptionObject(throwable).getId());

    TreeNode parent = treeNodes.pop();
    parent.getChildrenIds().add(latest.getId());
    latest = parent;
  }

  public TreeNode getRoot() {
    return root;
  }
}
