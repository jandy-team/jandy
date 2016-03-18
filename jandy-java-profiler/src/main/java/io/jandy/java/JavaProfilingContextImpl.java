package io.jandy.java;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.com.google.gson.Gson;
import io.jandy.java.data.*;
import io.jandy.java.key.ClassKey;
import io.jandy.java.key.MethodKey;
import io.jandy.java.profiler.MethodHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public class JavaProfilingContextImpl extends ThreadLocal<MethodHandler> implements JavaProfilingContext {

  private final List<TreeNode> roots = new ArrayList<TreeNode>();

  private final List<TreeNode> nodes = new ArrayList<TreeNode>();
  private final Map<MethodKey, MethodObject> methods = new HashMap<MethodKey, MethodObject>();
  private final Map<ClassKey, ClassObject> classes = new HashMap<ClassKey, ClassObject>();
  private final List<ExceptionObject> exceptions = new ArrayList<ExceptionObject>();

  public JavaProfilingContextImpl() {
    super();
  }

  public TreeNode getTreeNode(ClassType classType, MethodType methodType) {
    TreeNode n = new TreeNode();
    n.setId(UUID.randomUUID().toString());
    n.setMethodId(getMethodObject(classType, methodType).getId());
    n.setAcc(new Accumulator());
    n.setRoot(false);
    synchronized(nodes) {
      nodes.add(n);
    }

    return n;
  }

  public MethodObject getMethodObject(ClassType classType, MethodType methodType) {
    MethodKey key = new MethodKey(methodType, classType);
    synchronized(methods) {
      MethodObject m = methods.get(key);

      if (m == null) {
        m = new MethodObject();
        m.setId(UUID.randomUUID().toString());
        m.setOwnerId(getClassObject(classType).getId());
        m.setName(methodType.getName());
        m.setAccess(methodType.getAccess());
        m.setDescriptor(methodType.getDescriptor());
        methods.put(key, m);
      }

      return m;
    }
  }

  public ClassObject getClassObject(ClassType classType) {
    ClassKey key = new ClassKey(classType);
    synchronized(classes) {
      ClassObject c = classes.get(key);
      if (c == null) {
        c = new ClassObject();
        c.setId(UUID.randomUUID().toString());
        c.setName(classType.getName());
        c.setPackageName(classType.getPackageName());
        classes.put(key, c);
      }

      return c;
    }
  }

  public ExceptionObject getExceptionObject(Throwable throwable) {
    ExceptionObject e = new ExceptionObject();
    e.setId(UUID.randomUUID().toString());
    e.setClassId(getClassObject(throwable.getClass().getName()).getId());
    e.setMessage(throwable.getMessage());
    synchronized(exceptions) {
      exceptions.add(e);
    }

    return e;
  }

  public ClassObject getClassObject(String className) {

    String packageName = (className.substring(0, className.lastIndexOf('.')));
    String name = (className.substring(className.lastIndexOf('.') + 1));
    ClassKey key = new ClassKey(new ClassType(packageName, name));

    synchronized(classes) {
      ClassObject c = classes.get(key);
      if (c == null) {
        c = new ClassObject();
        c.setId(UUID.randomUUID().toString());
        c.setPackageName(packageName);
        c.setName(name);
        classes.put(key, c);
      }

      return c;
    }
  }

  private ProfilingContext getProfilingContext() {
    ProfilingContext m = new ProfilingContext();

    m.setNodes(nodes);
    m.setMethods(new ArrayList<MethodObject>());
    m.getMethods().addAll(methods.values());
    m.setClasses(new ArrayList<ClassObject>());
    m.getClasses().addAll(classes.values());
    m.setExceptions(exceptions);

    m.setRoot(new TreeNode());
    m.getRoot().setRoot(true);
    for (TreeNode n : roots) {
      if (n.getChildrenIds() != null && !n.getChildrenIds().isEmpty()) {
        m.getRoot().getChildrenIds().add(n.getChildrenIds().get(0));
      }
    }

    return m;
  }

  @Override
  public void write() {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter("java-profiler-result.jandy"));
      new Gson().toJson(getProfilingContext(), writer);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null)
        try { writer.close(); } catch (IOException ignored) {}
    }
  }

  @Override
  protected MethodHandler initialValue() {
    MethodHandler handler = new MethodHandler(this);
    roots.add(handler.getRoot());
    return handler;
  }
}
