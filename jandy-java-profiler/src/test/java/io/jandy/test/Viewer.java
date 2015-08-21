package io.jandy.test;


import io.jandy.org.apache.thrift.protocol.TCompactProtocol;
import io.jandy.org.apache.thrift.transport.TIOStreamTransport;
import io.jandy.thrift.java.ProfilingMetrics;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Viewer {
  public static void main(String[] args) throws Exception {
    String filename = "java-profiler-result.jandy";

    GZIPInputStream ois = new GZIPInputStream(new FileInputStream(filename));
    ProfilingMetrics metrics = new ProfilingMetrics();
    metrics.read(new TCompactProtocol(new TIOStreamTransport(ois)));

    System.out.println(metrics);
  }
}
