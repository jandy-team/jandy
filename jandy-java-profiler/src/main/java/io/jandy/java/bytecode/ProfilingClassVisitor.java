package io.jandy.java.bytecode;

import io.jandy.java.org.objectweb.asm.ClassVisitor;
import io.jandy.java.org.objectweb.asm.MethodVisitor;
import io.jandy.java.org.objectweb.asm.Opcodes;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ProfilingClassVisitor extends ClassVisitor {
  private String className;
  private int modified = -1;

  public ProfilingClassVisitor(ClassVisitor cv) {
    super(Opcodes.ASM5, cv);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);

    if (version < Opcodes.V1_8 && (access & Opcodes.ACC_INTERFACE) != 0)
      modified = 0;
    this.className = name;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

    if (modified == 0)
      return methodVisitor;

    if ((access & Opcodes.ACC_ABSTRACT) != 0 || (access & Opcodes.ACC_NATIVE) != 0) {
      if (modified == -1)
        modified = 0;
      return methodVisitor;
    } else {
      modified = 1;
      return new ProfilingMethodVisitor(methodVisitor, className, access, name, desc);
    }
  }

  public boolean isModified() {
    return modified == 1;
  }
}
