package io.jandy.test;



import io.jandy.com.google.gson.Gson;
import io.jandy.java.data.ProfilingContext;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Viewer {
  public static void main(String[] args) throws Exception {
    String filename = "java-profiler-result.jandy";

    BufferedReader reader = new BufferedReader(new FileReader(filename));
    ProfilingContext metrics = new Gson().fromJson(reader, ProfilingContext.class);

    System.out.println(metrics);
  }
}
