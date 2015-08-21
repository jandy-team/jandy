package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import com.github.jcooky.jaal.common.profile.Profiler;
import io.jandy.java.metrics.MetricsFactory;
import io.jandy.org.apache.thrift.TException;
import io.jandy.org.apache.thrift.protocol.TCompactProtocol;
import io.jandy.org.apache.thrift.transport.TIOStreamTransport;
import io.jandy.thrift.java.ProfilingMetrics;

import java.io.*;
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
        GZIPOutputStream dos = null;
        try {
          dos = new GZIPOutputStream(new FileOutputStream("java-profiler-result.jandy"));
          ProfilingMetrics metrics = MetricsFactory.getProfilingMetrics(collectorFactory.getRoots());
          metrics.write(new TCompactProtocol(new TIOStreamTransport(dos)));
          dos.flush();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (TException e) {
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
