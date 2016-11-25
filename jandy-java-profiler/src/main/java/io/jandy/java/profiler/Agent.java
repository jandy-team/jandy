package io.jandy.java.profiler;

import io.jandy.java.bytecode.ProfilingClassFileTransformer;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Agent {

  public static void premain(String agentArgs, Instrumentation inst) {
    inst.addTransformer(new ProfilingClassFileTransformer());
  }
}
