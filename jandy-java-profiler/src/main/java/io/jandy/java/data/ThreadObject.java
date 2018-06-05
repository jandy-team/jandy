package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-06-30
 */
public class ThreadObject {
  private long threadId;
  private String threadName;
  private String rootId;

  public String getThreadName() {
    return threadName;
  }

  public ThreadObject setThreadName(String threadName) {
    this.threadName = threadName;
    return this;
  }

  public long getThreadId() {
    return threadId;
  }

  public ThreadObject setThreadId(long threadId) {
    this.threadId = threadId;
    return this;
  }

  public String getRootId() {
    return rootId;
  }

  public ThreadObject setRootId(String rootId) {
    this.rootId = rootId;
    return this;
  }

}
