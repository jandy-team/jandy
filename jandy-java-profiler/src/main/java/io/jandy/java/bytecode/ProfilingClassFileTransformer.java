package io.jandy.java.bytecode;

import io.jandy.java.org.objectweb.asm.ClassReader;
import io.jandy.java.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author JCooky
 * @since 2016-05-23
 */
public class ProfilingClassFileTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    className = className.replace('/', '.');
    if (!className.startsWith("io.jandy.java")
     && !className.startsWith("java.") && !className.startsWith("com.sun.") && !className.startsWith("javax.") && !className.startsWith("sun.")
     && !className.startsWith("jdk.internal")
        ) {
      ClassReader cr = new ClassReader(classfileBuffer);

      ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
      ProfilingClassVisitor profilingClassVisitor = new ProfilingClassVisitor(cw);

      cr.accept(profilingClassVisitor, ClassReader.EXPAND_FRAMES);

      if (profilingClassVisitor.isModified())
        return cw.toByteArray();
    }

    return null;
  }
}
