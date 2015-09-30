package io.jandy.java;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;
import io.jandy.java.key.ClassKey;
import io.jandy.java.key.MethodKey;
import io.jandy.java.profiler.MethodHandler;
import io.jandy.org.apache.thrift.TException;
import io.jandy.org.apache.thrift.protocol.TCompactProtocol;
import io.jandy.org.apache.thrift.protocol.TJSONProtocol;
import io.jandy.org.apache.thrift.protocol.TProtocol;
import io.jandy.org.apache.thrift.transport.TSimpleFileTransport;
import io.jandy.org.apache.thrift.transport.TTransport;
import io.jandy.thrift.java.*;

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
    n.id = UUID.randomUUID().toString();
    n.methodId = getMethodObject(classType, methodType).getId();
    n.acc = new Accumulator();
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
        m.id = UUID.randomUUID().toString();
        m.ownerId = getClassObject(classType).getId();
        m.name = methodType.getName();
        m.access = methodType.getAccess();
        m.descriptor = methodType.getDescriptor();
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
        c.id = UUID.randomUUID().toString();
        c.name = classType.getName();
        c.packageName = classType.getPackageName();
        classes.put(key, c);
      }

      return c;
    }
  }

  public ExceptionObject getExceptionObject(Throwable throwable) {
    ExceptionObject e = new ExceptionObject();
    e.id = UUID.randomUUID().toString();
    e.classId = getClassObject(throwable.getClass().getName()).getId();
    e.message = throwable.getMessage();
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
        c.id = UUID.randomUUID().toString();
        c.packageName = packageName;
        c.name = name;
        classes.put(key, c);
      }

      return c;
    }
  }

  private ProfilingContext getProfilingContext() {
    ProfilingContext m = new ProfilingContext();

    m.nodes = nodes;
    m.methods = new ArrayList<MethodObject>();
    m.methods.addAll(methods.values());
    m.classes = new ArrayList<ClassObject>();
    m.classes.addAll(classes.values());
    m.exceptions = exceptions;

    m.root = new TreeNode();
    for (TreeNode n : roots) {
      if (n.getChildrenIds() != null && !n.getChildrenIds().isEmpty()) {
        m.getRoot().addToChildrenIds(n.getChildrenIds().get(0));
      }
    }

    return m;
  }

  @Override
  public void write(TProtocol protocol) {
    TTransport transport = null;
    try {
      transport = new TSimpleFileTransport("java-profiler-result.jandy", false, true);
      getProfilingContext().write(new TJSONProtocol(transport));
      transport.flush();
    } catch (TException e) {
      e.printStackTrace();
    } finally {
      if (transport != null)
        transport.close();
    }
  }

  @Override
  protected MethodHandler initialValue() {
    MethodHandler handler = new MethodHandler(this);
    roots.add(handler.getRoot());
    return handler;
  }
}
