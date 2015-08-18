package io.jandy.java.profiler;

import com.github.jcooky.jaal.agent.JaalAgentBootstrap;
import com.github.jcooky.jaal.agent.criteria.Restrictions;
import com.github.jcooky.jaal.common.profile.Profiler;

import java.lang.instrument.Instrumentation;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class Agent {
  private static Profiler PROFILER = new JandyProfiler();

  public static Profiler getProfiler() {
    return PROFILER;
  }

  public static void premain(String agentArgs, Instrumentation inst) {
    new JaalAgentBootstrap().setName("jandy-java-profiler")
        .excludePackage("io.jandy")
        .addProfilingInjectorStrategy(Restrictions.any(), Agent.class)
        .build(inst);
  }
}
