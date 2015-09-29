package io.jandy.java;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.profiler.MethodHandler;
import io.jandy.org.apache.thrift.protocol.TProtocol;
import io.jandy.thrift.java.*;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public interface JavaProfilingContext {
  TreeNode getTreeNode(ClassType classType, MethodType methodType);
  MethodObject getMethodObject(ClassType classType, MethodType methodType);
  ClassObject getClassObject(ClassType classType);
  ExceptionObject getExceptionObject(Throwable throwable);
  ClassObject getClassObject(String className);

  MethodHandler get();

  void write(TProtocol protocol);
}
