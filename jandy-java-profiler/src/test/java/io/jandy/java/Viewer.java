package io.jandy.java;

import io.jandy.java.metrics.ProfilingMetrics;
import io.jandy.java.metrics.TreeNode;

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

    ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(filename)));
    ProfilingMetrics metrics = (ProfilingMetrics) ois.readObject();

    System.out.println(metrics.getRoot());
  }
}
