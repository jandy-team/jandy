package io.jandy.java;

import io.jandy.java.data.*;
import io.jandy.java.key.ClassKey;

import java.util.*;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public abstract class DataObjectBuilder {

  public abstract void save(TreeNode node);

  public TreeNode getRootTreeNode() {
    TreeNode n = new TreeNode();
    n.setId(UUID.randomUUID().toString());
    n.setMethod(null);
    n.setRoot(true);
    n.setAcc(null);

    return n;
  }

  public TreeNode getTreeNode(String className, int access, String methodName, String desc, String parentId) {
    TreeNode n = new TreeNode();
    n.setId(UUID.randomUUID().toString());
    n.setMethod(getMethodObject(className, access, methodName, desc));
    n.setAcc(new Accumulator());
    n.setRoot(false);
    n.setParentId(parentId);

    return n;
  }

  public MethodObject getMethodObject(String className, int access, String methodName, String desc) {
    MethodObject m = new MethodObject();
    m.setId(UUID.randomUUID().toString());
    m.setOwner(getClassObject(className));
    m.setName(methodName);
    m.setAccess(access);
    m.setDescriptor(desc);

    return m;
  }

  public ClassObject getClassObject(String className) {

    ClassKey key = new ClassKey(className);
    ClassObject c = new ClassObject();
    c.setId(UUID.randomUUID().toString());
    c.setName(key.getClassName());
    c.setPackageName(key.getPackageName());

    return c;
  }

  public ExceptionObject getExceptionObject(Throwable throwable) {
    ExceptionObject e = new ExceptionObject();
    e.setId(UUID.randomUUID().toString());
    e.setKlass(getClassObject(throwable.getClass().getName()));
    e.setMessage(throwable.getMessage());

    return e;
  }

//  public static ProfilingContext merge(Iterable<ProfilingContext> contexts) {
//    TreeNode root = new TreeNode();
//    root.setId(UUID.randomUUID().toString());
//    root.setRoot(true);
//    for (ProfilingContext context : contexts) {
//      assert (context.getRootId().equals(context.getNodes().get(0).getId()));
//      context.getNodes().get(0).setParentId(root.getId());
//    }
//
//    List<TreeNode> nodes = new ArrayList<TreeNode>();
//    Set<MethodObject> methods = new HashSet<MethodObject>();
//    Set<ClassObject> classes = new HashSet<ClassObject>();
//    List<ExceptionObject> exceptions = new ArrayList<ExceptionObject>();
//
//    nodes.add(0, root);
//    for (ProfilingContext context : contexts) {
//      nodes.addAll(context.getNodes());
//      methods.addAll(context.getMethods());
//      classes.addAll(context.getClasses());
//      exceptions.addAll(context.getExceptions());
//    }
//
//    ProfilingContext m = new ProfilingContext();
//    m.setNodes(nodes);
//    m.setMethods(new ArrayList<MethodObject>(methods));
//    m.setClasses(new ArrayList<ClassObject>(classes));
//    m.setExceptions(exceptions);
//
//    m.setRootId(root.getId());
//
//    return m;
//  }
}
