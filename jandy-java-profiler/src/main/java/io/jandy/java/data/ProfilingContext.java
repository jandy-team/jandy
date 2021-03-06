package io.jandy.java.data;

import java.util.List;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class ProfilingContext {
  private long profId;
  private List<ThreadObject> threadObjects;
  
  public long getProfId() {
    return profId;
  }

  public ProfilingContext setProfId(long profId) {
    this.profId = profId;
    return this;
  }

  public List<ThreadObject> getThreadObjects() {
    return threadObjects;
  }

  public ProfilingContext setThreadObjects(List<ThreadObject> threadObjects) {
    this.threadObjects = threadObjects;
    return this;
  }
}
