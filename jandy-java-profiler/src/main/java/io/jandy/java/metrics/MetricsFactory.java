package io.jandy.java.metrics;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.thrift.java.*;

import java.util.List;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MetricsFactory {
  public static TreeNode getTreeNode(ClassType classType, MethodType methodType) {
    TreeNode n = new TreeNode();
    n.method = getMethodKey(classType, methodType);
    n.acc = new Accumulator();

    return n;
  }

  public static MethodKey getMethodKey(ClassType classType, MethodType methodType) {
    MethodKey m = new MethodKey();
    m.owner = getClassKey(classType);
    m.name = methodType.getName();
    m.access = methodType.getAccess();
    m.descriptor = methodType.getDescriptor();
    return m;
  }

  private static ClassKey getClassKey(ClassType classType) {
    ClassKey c = new ClassKey();
    c.name = classType.getName();
    c.packageName = classType.getPackageName();

    return c;
  }

  public static ExceptionKey getExceptionKey(Throwable throwable) {
    ExceptionKey e = new ExceptionKey();
    e.caused = (throwable.getCause() == null ? null : getExceptionKey(throwable.getCause()));
    e.classKey = (getClassKey(throwable.getClass().getName()));

    return e;
  }

  private static ClassKey getClassKey(String className) {
    ClassKey c = new ClassKey();
    c.packageName = (className.substring(0, className.lastIndexOf('.')));
    c.name = (className.substring(className.lastIndexOf('.') + 1));

    return c;
  }

  public static ProfilingMetrics getProfilingMetrics(List<TreeNode> roots) {
    ProfilingMetrics m = new ProfilingMetrics();
    m.setRoot(new TreeNode());
    for (TreeNode n : roots) {
      if (n.getChildren() != null && !n.getChildren().isEmpty()) {
        m.getRoot().addToChildren(n.getChildren().get(0));
      }
    }

    return m;
  }
}
