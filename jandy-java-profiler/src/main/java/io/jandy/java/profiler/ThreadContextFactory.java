package io.jandy.java.profiler;

import io.jandy.java.JandyProfilingContext;
import io.jandy.java.data.ThreadObject;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ThreadContextFactory extends ThreadLocal<ThreadContext> {
  private JandyProfilingContext profilingContext = new JandyProfilingContext();
  private List<ThreadContext> threadContexts = Collections.synchronizedList(new ArrayList<ThreadContext>());

  public ThreadContextFactory() {
    super();

    try {
      profilingContext.start();

      Runtime.getRuntime().addShutdownHook(new Thread() {
        public void run() {
          for (ThreadContext tc : threadContexts) {
            tc.onExit();
          }

          profilingContext.end(threadContexts);
        }
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected ThreadContext initialValue() {
    ThreadContext tc = null;
    try {
      Thread currentThread = Thread.currentThread();
      tc = new ThreadContext(currentThread.getId(), currentThread.getName(), profilingContext.getBuilder());
      threadContexts.add(tc);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return tc;
  }

}
