package io.jandy.test;


import io.jandy.org.apache.thrift.protocol.TCompactProtocol;
import io.jandy.org.apache.thrift.transport.TIOStreamTransport;
import io.jandy.thrift.java.ProfilingContext;

import java.io.FileInputStream;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Viewer {
  public static void main(String[] args) throws Exception {
    String filename = "java-profiler-result.jandy";

    ProfilingContext metrics = new ProfilingContext();
    metrics.read(new TCompactProtocol(new TIOStreamTransport(new FileInputStream(filename))));

    System.out.println(metrics);
  }
}
