package io.jandy.java.metrics;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class TreeNodeFactory {
  public static TreeNode getTreeNode(ClassType classType, MethodType methodType) {
    TreeNode n = new TreeNode();
    n.setMethodKey(getMethodKey(classType, methodType));

    return n;
  }

  public static MethodKey getMethodKey(ClassType classType, MethodType methodType) {
    MethodKey m = new MethodKey();
    m.setOwner(getClassKey(classType));
    m.setName(methodType.getName());
    m.setAccess(m.getAccess());
    m.setDescriptor(m.getDescriptor());
    return m;
  }

  private static ClassKey getClassKey(ClassType classType) {
    ClassKey c = new ClassKey();
    c.setName(classType.getName());
    c.setPackageName(classType.getPackageName());

    return c;
  }

  public static ExceptionKey getExceptionKey(Throwable throwable) {
    ExceptionKey e = new ExceptionKey();
    e.setCausedBy(throwable.getCause() == null ? null : getExceptionKey(throwable.getCause()));
    e.setClassKey(getClassKey(throwable.getClass().getName()));

    return e;
  }

  private static ClassKey getClassKey(String className) {
    ClassKey c = new ClassKey();
    c.setPackageName(className.substring(0, className.lastIndexOf('.')));
    c.setName(className.substring(className.lastIndexOf('.')+1));

    return c;
  }
}
