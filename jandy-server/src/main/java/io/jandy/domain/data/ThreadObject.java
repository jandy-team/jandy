package io.jandy.domain.data;

import java.util.UUID;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class ThreadObject {
  private String id = UUID.randomUUID().toString();
  private long threadId;
  private String threadName;
  private String rootId;

  public long getThreadId() {
    return threadId;
  }

  public ThreadObject setThreadId(long threadId) {
    this.threadId = threadId;
    return this;
  }

  public String getThreadName() {
    return threadName;
  }

  public ThreadObject setThreadName(String threadName) {
    this.threadName = threadName;
    return this;
  }

  public String getRootId() {
    return rootId;
  }

  public ThreadObject setRootId(String rootId) {
    this.rootId = rootId;
    return this;
  }

  public String getId() {
    return id;
  }

  public ThreadObject setId(String id) {
    this.id = id;
    return this;
  }
}
