package io.jandy.java.profiler;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import com.github.jcooky.jaal.common.profile.Profiler;
import io.jandy.java.metrics.MetricsFactory;
import io.jandy.org.apache.thrift.TException;
import io.jandy.org.apache.thrift.protocol.TCompactProtocol;
import io.jandy.org.apache.thrift.transport.TSimpleFileTransport;
import io.jandy.org.apache.thrift.transport.TTransport;
import io.jandy.thrift.java.ProfilingMetrics;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class JandyProfiler implements Profiler {
  private MethodHandlerContext context = new MethodHandlerContext();

  public JandyProfiler() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        TTransport transport = null;
        try {
          transport = new TSimpleFileTransport("java-profiler-result.jandy", false, true);
          ProfilingMetrics metrics = MetricsFactory.getProfilingMetrics(context.getRoots());
          metrics.write(new TCompactProtocol(transport));
          transport.flush();
        } catch (TException e) {
          e.printStackTrace();
        } finally {
          if (transport != null)
            transport.close();
        }
      }
    });
  }

  public void begin(ClassType owner, MethodType method) {
    context.get().enter(owner, method);
  }

  public void end(ClassType owner, MethodType method, long startTime, Throwable throwable, long elapsedTime) {
    context.get().exit(owner, method, startTime, throwable, elapsedTime);
  }
}
