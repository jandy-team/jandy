package io.jandy.java;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.data.ClassObject;
import io.jandy.java.data.ExceptionObject;
import io.jandy.java.data.MethodObject;
import io.jandy.java.data.TreeNode;
import io.jandy.java.profiler.MethodHandler;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public interface JavaProfilingContext {
  TreeNode getTreeNode(ClassType classType, MethodType methodType, String parentId);
  MethodObject getMethodObject(ClassType classType, MethodType methodType);
  ClassObject getClassObject(ClassType classType);
  ExceptionObject getExceptionObject(Throwable throwable);
  ClassObject getClassObject(String className);

  MethodHandler get();

  void write();
}
