package io.jandy.java.profiler;

import io.jandy.java.JandyProfilingContext;

import java.io.IOException;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ThreadContextFactory extends ThreadLocal<ThreadContext> {
  private JandyProfilingContext profilingContext = new JandyProfilingContext();
//  private List<ThreadContext> threadContexts = Collections.synchronizedList(new ArrayList<ThreadContext>());

  public ThreadContextFactory() {
    super();

    profilingContext.start();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        profilingContext.end();
      }
    });
  }

  @Override
  protected ThreadContext initialValue() {
    ThreadContext tc = null;
    try {
      Thread currentThread = Thread.currentThread();
      tc = new ThreadContext(currentThread.getId(), currentThread.getName(), profilingContext.getBuilder(currentThread.getId()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return tc;
  }

}
