package io.jandy.java.profiler;

import io.jandy.java.DataObjectBuilder;
import io.jandy.java.data.ThreadObject;
import io.jandy.java.data.TreeNode;
import io.jandy.java.util.ArrayCallStack;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ThreadContext {
  private final DataObjectBuilder builder;
  private TreeNode latest, root;
  private ArrayCallStack treeNodes = new ArrayCallStack(128);
  private ThreadObject thread = new ThreadObject();

  public ThreadContext(long threadId, String threadName, DataObjectBuilder builder) throws IOException {
    this.thread.setThreadId(threadId);
    this.thread.setThreadName(threadName);
    this.builder = builder;

    this.latest = builder.getRootTreeNode();
    this.root = this.latest;
    this.thread.setRootId(this.root.getId());

    builder.save(this.latest);
  }

  public void onMethodEnter(String className, int access, String methodName, String desc) {
    treeNodes.push(latest);

    latest = this.builder.getTreeNode(className.replace('/', '.'), access, methodName, desc, latest.getId());

    latest.getAcc().setStartTime(System.nanoTime());
  }

  public void onMethodExit(Throwable throwable) {
    //    assert(latest.method.getName().equals(method.getName()));
    //    assert(latest.method.getDescriptor().equals(method.getDescriptor()));
    //    assert(latest.method.getAccess() == method.getAccess());
    //    assert(latest.method.getOwner().getName().equals(owner.getName()));
    //    assert(latest.method.getOwner().getPackageName().equals(owner.getPackageName()));

    long elapsedTime = System.nanoTime() - latest.getAcc().getStartTime();
    latest.getAcc().setElapsedTime(latest.getAcc().getElapsedTime() + elapsedTime);
    if (throwable != null)
      latest.getAcc().setException(builder.getExceptionObject(throwable));

    long time = System.nanoTime();
    builder.save(latest);
    time = System.nanoTime() - time;

    latest = treeNodes.pop();
    if (latest.getAcc() != null) {
      latest.getAcc().setElapsedTime(latest.getAcc().getElapsedTime() - time);
    }
  }

  public void onExit() {
    while (latest != root)
      onMethodExit(null);
  }

  public TreeNode getRoot() {
    return root;
  }

  public ThreadObject getThreadObject() {
    return thread;
  }
}
