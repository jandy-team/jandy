package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import com.github.jcooky.jaal.common.profile.Profiler;
import io.jandy.java.metrics.ProfilingMetrics;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class JandyProfiler implements Profiler {
  private MethodCollectorThreadLocalFactory collectorFactory = new MethodCollectorThreadLocalFactory();

  public JandyProfiler() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        ObjectOutputStream dos = null;
        try {
          dos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("java-profiler-result.jandy")));
          ProfilingMetrics metrics = new ProfilingMetrics(collectorFactory.getRoots());
          dos.writeObject(metrics);
          dos.flush();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          if (dos != null)
            try {
              dos.close();
            } catch (IOException e) {
            }
        }
      }
    });
  }

  public void begin(ClassType owner, MethodType method, long startTime) {
    collectorFactory.get().enter(owner, method, startTime);
  }

  public void end(ClassType owner, MethodType method, long startTime, Throwable throwable, long elapsedTime) {
    collectorFactory.get().exit(owner, method, throwable, elapsedTime);
  }
}
