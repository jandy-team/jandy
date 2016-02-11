package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.thrift.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Service
public class ProfContextBuilder {
  private static final Logger logger = LoggerFactory.getLogger(ProfContextDump.class);

  @Autowired
  private ProfTreeNodeRepository profTreeNodeRepository;
  @Autowired
  private ProfMethodRepository profMethodRepository;
  @Autowired
  private ProfClassRepository profClassRepository;
  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;

  private class Context {
    public Map<String, TreeNode> nodes;
    public Map<String, MethodObject> methods;
    public Map<String, ClassObject> classes;
    public Map<String, ExceptionObject> exceptions;

    public Context(Map<String, TreeNode> nodes, Map<String, MethodObject> methods, Map<String, ClassObject> classes, Map<String, ExceptionObject> exceptions) {
      this.nodes = nodes;
      this.methods = methods;
      this.classes = classes;
      this.exceptions = exceptions;
    }
  }

  @Async
  public ListenableFuture<Map<String, ProfContextDump>> buildForAsync(Map<String, ProfilingContext> contexts) {
    Map<String, ProfContextDump> r = new HashMap<>();
    for (String id : contexts.keySet()) {
      r.put(id, build(contexts.get(id)));
    }
    return new AsyncResult<>(r);
  }

  @Transactional
  public ProfContextDump build(ProfilingContext context) {
    Map<String, TreeNode> nodes = context.nodes.stream().collect(Collectors.toMap(TreeNode::getId, Function.identity()));
    Map<String, MethodObject> methods = context.methods.stream().collect(Collectors.toMap(MethodObject::getId, Function.identity()));
    Map<String, ClassObject> classes = context.classes.stream().collect(Collectors.toMap(ClassObject::getId, Function.identity()));
    Map<String, ExceptionObject> exceptions = context.exceptions.stream().collect(Collectors.toMap(ExceptionObject::getId, Function.identity()));

    ProfContextDump dump = new ProfContextDump();
    dump.setRoot(buildTreeNode(new Context(nodes, methods, classes, exceptions), context.getRoot(), null));
    dump.setMaxTotalDuration(stream(dump.spliterator(), false).mapToLong(ProfTreeNode::getElapsedTime).max().getAsLong());
    return profContextDumpRepository.save(dump);
  }

  @Transactional
  public ProfTreeNode buildTreeNode(Context context, TreeNode treeNode, ProfTreeNode parent) {
    ProfTreeNode profTreeNode = new ProfTreeNode();

    Accumulator acc = treeNode.getAcc();
    if (acc != null) {
      profTreeNode.setElapsedTime(acc.getElapsedTime());
      profTreeNode.setStartTime(acc.getStartTime());
      profTreeNode.setConcurThreadName(acc.getConcurThreadName());
    }
    profTreeNode.setRoot(treeNode.isRoot());
    profTreeNode.setMethod(treeNode.methodId == null ? null : getProfMethod(context, context.methods.get(treeNode.methodId)));
    profTreeNode.setParent(parent);

    profTreeNode = profTreeNodeRepository.save(profTreeNode);

    logger.trace("Building profiling tree node: {}", profTreeNode);

    if (treeNode.isSetChildrenIds()) {
      for (String nodeId : treeNode.childrenIds) {
        TreeNode childNode = context.nodes.get(nodeId);
        profTreeNode.getChildren().add(buildTreeNode(context, childNode, profTreeNode));
      }
    }

    return profTreeNode;
  }

  private ProfMethod getProfMethod(Context context, MethodObject methodKey) {
    ProfClass profClass = getProfClass(context.classes.get(methodKey.ownerId));
    ProfMethod profMethod = profMethodRepository.findByNameAndDescriptorAndOwner_Id(methodKey.getName(), methodKey.getDescriptor(), profClass.getId());
    return profMethod == null ? buildProfMethod(methodKey, profClass) : profMethod;
  }

  @Transactional
  private ProfMethod buildProfMethod(MethodObject methodObject, ProfClass profClass) {
    ProfMethod method = new ProfMethod();
    method.setOwner(profClass);
    method.setName(methodObject.getName());
    method.setDescriptor(methodObject.getDescriptor());
    method.setAccess(methodObject.getAccess());
    method.setOwner(profClass);

    return profMethodRepository.save(method);
  }

  private ProfClass getProfClass(ClassObject classObject) {
    ProfClass profClass = profClassRepository.findByNameAndPackageName(classObject.getName(), classObject.getPackageName());
    return profClass == null ? buildProfClass(classObject.getName(), classObject.getPackageName()) : profClass;
  }

  @Transactional
  private ProfClass buildProfClass(String name, String packageName) {
    ProfClass profClass = new ProfClass();
    profClass.setName(name);
    profClass.setPackageName(packageName);

    return profClassRepository.save(profClass);
  }
}
