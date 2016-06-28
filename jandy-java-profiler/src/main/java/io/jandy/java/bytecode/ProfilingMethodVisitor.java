package io.jandy.java.bytecode;

import io.jandy.java.org.objectweb.asm.MethodVisitor;
import io.jandy.java.org.objectweb.asm.Opcodes;
import io.jandy.java.org.objectweb.asm.Type;
import io.jandy.java.org.objectweb.asm.commons.AdviceAdapter;
import io.jandy.java.org.objectweb.asm.commons.Method;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ProfilingMethodVisitor extends AdviceAdapter {

  private int exception;

  private final String className;
  private final String name;

  /**
   * Creates a new {@link AdviceAdapter}.
   *
   * @param api    the ASM API version implemented by this visitor. Must be one
   *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
   * @param mv     the method visitor to which this adapter delegates calls.
   * @param access the method's access flags (see {@link Opcodes}).
   * @param name   the method's name.
   * @param desc   the method's descriptor (see {@link Type Type}).
   */
  protected ProfilingMethodVisitor(MethodVisitor mv, String className, int access, String name, String desc) {
    super(Opcodes.ASM5, mv, access, name, desc);

    this.className = className;
    this.name = name;
  }

  @Override
  protected void onMethodEnter() {
    super.push(className);
    super.push(methodAccess);
    super.push(name);
    super.push(methodDesc);
    super.invokeStatic(JandyProfilingAdvicer.TYPE, JandyProfilingAdvicer.begin);

//    // Throwable exception = null
//    exception = super.newLocal(Throwable.TYPE);
//    visitInsn(ACONST_NULL);
//    storeLocal(exception);
  }

  @Override
  protected void onMethodExit(int opcode) {
    if (opcode == ARETURN || opcode == DRETURN || opcode == IRETURN || opcode == FRETURN || opcode == LRETURN || opcode == RETURN) {
      super.visitInsn(ACONST_NULL);
      super.push(className);
      super.push(methodAccess);
      super.push(name);
      super.push(methodDesc);
      super.invokeStatic(JandyProfilingAdvicer.TYPE, JandyProfilingAdvicer.end);
    } else if (opcode == ATHROW){
      dup();
      super.push(className);
      super.push(methodAccess);
      super.push(name);
      super.push(methodDesc);
      super.invokeStatic(JandyProfilingAdvicer.TYPE, JandyProfilingAdvicer.end);
    }
  }

  private interface JandyProfilingAdvicer {
//    Class<?> CLASS = io.jandy.java.profiler.JandyProfilingAdvicer.class;
    Type TYPE = Type.getType("Lio/jandy/java/profiler/JandyProfilingAdvicer;");

    Method begin = Method.getMethod("void begin(String, int, String, String)");
    Method end = Method.getMethod("void end(java.lang.Throwable, String, int, String, String)");
  }

  private interface Throwable {
    Class<?> CLASS = java.lang.Throwable.class;
    Type TYPE = Type.getType(CLASS);
  }
}
