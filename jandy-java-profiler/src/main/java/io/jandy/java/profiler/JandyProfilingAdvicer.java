package io.jandy.java.profiler;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class JandyProfilingAdvicer {
  private static ThreadContextFactory factory = new ThreadContextFactory();

  public static void begin(String className, int access, String methodName, String desc) {
    factory.get().onMethodEnter(className, access, methodName, desc);
  }

  public static void end(Throwable throwable, String className, int access, String methodName, String desc) {
    factory.get().onMethodExit(throwable, className, access, methodName, desc);
  }
}
