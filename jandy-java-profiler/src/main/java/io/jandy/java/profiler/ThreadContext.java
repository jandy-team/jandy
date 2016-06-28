package io.jandy.java.profiler;

import io.jandy.java.DataObjectBuilder;
import io.jandy.java.data.TreeNode;
import io.jandy.java.util.ArrayCallStack;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ThreadContext {
  private final DataObjectBuilder builder;
  private TreeNode latest = new TreeNode(), root = null;
  private ArrayCallStack treeNodes = new ArrayCallStack(128);
  private String threadName;
  private long threadId, profId;

  public ThreadContext(long profId, long threadId, String threadName, DataObjectBuilder builder) throws IOException {
    this.threadId = threadId;
    this.profId = profId;
    this.threadName = threadName;
    this.builder = builder;
  }

  public void onMethodEnter(String className, int access, String methodName, String desc) {
    treeNodes.push(latest);

    latest = this.builder.getTreeNode(className.replace('/', '.'), access, methodName, desc, latest.getId());
    if (root == null) {
      root = latest;
      root.setRoot(true);
    }

    latest.setProfId(profId);

    latest.getAcc().setConcurThreadName(threadName);
    latest.getAcc().setConcurThreadId(threadId);
    latest.getAcc().setStartTime(System.nanoTime());
  }

  public void onMethodExit(Throwable throwable, String className, int access, String methodName, String desc) {
    //    assert(latest.method.getName().equals(method.getName()));
    //    assert(latest.method.getDescriptor().equals(method.getDescriptor()));
    //    assert(latest.method.getAccess() == method.getAccess());
    //    assert(latest.method.getOwner().getName().equals(owner.getName()));
    //    assert(latest.method.getOwner().getPackageName().equals(owner.getPackageName()));

    long elapsedTime = System.nanoTime() - latest.getAcc().getStartTime();
    latest.getAcc().setElapsedTime(elapsedTime);
    if (throwable != null)
      latest.getAcc().setException(builder.getExceptionObject(throwable));
    builder.save(latest);

    latest = treeNodes.pop();
  }

  public String getThreadName() {
    return threadName;
  }

  public TreeNode getRoot() {
    return root;
  }

  public long getThreadId() {
    return threadId;
  }
}
