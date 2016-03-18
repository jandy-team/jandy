package io.jandy.service;

import com.google.common.cache.Cache;
import io.jandy.domain.*;
import io.jandy.java.data.*;
import io.jandy.util.NoopCache;
import io.jandy.util.SimpleMapCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;
import java.util.concurrent.ExecutionException;
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

//  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
//  private int batchSize;

  @Autowired
  private ProfTreeNodeRepository profTreeNodeRepository;
  @Autowired
  private ProfMethodRepository profMethodRepository;
  @Autowired
  private ProfClassRepository profClassRepository;
  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;


  @Async
  public ListenableFuture<Map<String, ProfContextDump>> buildForAsync(Map<String, ProfilingContext> contexts) throws ExecutionException {
    Map<String, ProfContextDump> r = new HashMap<>();
    for (String id : contexts.keySet()) {
      r.put(id, build(contexts.get(id)));
    }
    return new AsyncResult<>(r);
  }

  @Transactional
  public ProfContextDump build(ProfilingContext context) throws ExecutionException {
    Map<String, TreeNode> nodes = context.getNodes().stream().collect(Collectors.toMap(TreeNode::getId, Function.identity()));
    Map<String, MethodObject> methods = context.getMethods().stream().collect(Collectors.toMap(MethodObject::getId, Function.identity()));
    Map<String, ClassObject> classes = context.getClasses().stream().collect(Collectors.toMap(ClassObject::getId, Function.identity()));
    Map<String, ExceptionObject> exceptions = context.getExceptions().stream().collect(Collectors.toMap(ExceptionObject::getId, Function.identity()));

    ProfContextDump dump = new ProfContextDump();
    dump.setRoot(buildTreeNode(new Context(nodes, methods, classes, exceptions), context));
    dump.setMaxTotalDuration(stream(dump.spliterator(), false).mapToLong(ProfTreeNode::getElapsedTime).max().getAsLong());
    return profContextDumpRepository.save(dump);
  }

  @Transactional
  ProfTreeNode buildTreeNode(Context c, ProfilingContext pc) throws ExecutionException {
    Deque<Param> stack = new ArrayDeque<>();

    long startTime = System.currentTimeMillis();
    logger.trace("Start to build profiling context");
    int cnt = 0;
    ProfTreeNode root = null;
    stack.push(new Param(null, pc.getRoot()));
    while (stack.peek() != null) {
      Param param = stack.pop();
      ProfTreeNode cur = buildTreeNode(c, param.node, param.parent);

      if (root == null)
        root = cur;

      if (param.parent != null) {
        param.parent.getChildren().add(cur);
      }

      if (param.node.getChildrenIds() != null) {
        for (String nodeId : param.node.getChildrenIds()) {
          stack.push(new Param(cur, c.nodes.get(nodeId)));
        }
      }

      if (++cnt % 10000 == 0) {
        logger.trace("Building profiling tree node count: {}", cnt);
      }
    }
    logger.trace("Finish to build profiling context for {}ms", System.currentTimeMillis() - startTime);

    return root;
  }

  @Transactional
  public ProfTreeNode buildTreeNode(Context context, TreeNode treeNode, ProfTreeNode parent) throws ExecutionException {
    ProfTreeNode profTreeNode = new ProfTreeNode();

    Accumulator acc = treeNode.getAcc();
    if (acc != null) {
      profTreeNode.setElapsedTime(acc.getElapsedTime());
      profTreeNode.setStartTime(acc.getStartTime());
      profTreeNode.setConcurThreadName(acc.getConcurThreadName());
    }
    profTreeNode.setRoot(treeNode.isRoot());
    profTreeNode.setMethod(treeNode.getMethodId() == null ? null : getProfMethod(context, context.methods.get(treeNode.getMethodId())));
    profTreeNode.setParent(parent);

    return profTreeNodeRepository.save(profTreeNode);
  }

  @Transactional(readOnly = true)
  private ProfMethod getProfMethod(final Context context, final MethodObject methodKey) throws ExecutionException {
    ProfClass profClass = getProfClass(context, context.classes.get(methodKey.getOwnerId()));

    return context.profMethodCache.get(methodKey.getId(), () -> {
      ProfMethod profMethod = profMethodRepository.findOne(methodKey.getId());
      if (profMethod == null)
        profMethod = buildProfMethod(methodKey, profClass);

      context.profMethodCache.put(methodKey.getId(), profMethod);
      return profMethod;
    });
  }

  @Transactional
  private ProfMethod buildProfMethod(MethodObject methodObject, ProfClass profClass) {
    ProfMethod method = new ProfMethod();
    method.setId(methodObject.getId());
    method.setName(methodObject.getName());
    method.setDescriptor(methodObject.getDescriptor());
    method.setAccess(methodObject.getAccess());
    method.setOwner(profClass);

    return profMethodRepository.save(method);
  }

  @Transactional(readOnly = true)
  private ProfClass getProfClass(Context context, ClassObject classObject) throws ExecutionException {

    return context.profClassCache.get(classObject.getId(), () -> {
      ProfClass profClass = profClassRepository.findOne(classObject.getId());
      if (profClass == null)
        profClass = buildProfClass(classObject);

      context.profClassCache.put(classObject.getId(), profClass);
      return profClass;
    });
  }

  @Transactional
  private ProfClass buildProfClass(ClassObject classObject) {
    ProfClass profClass = new ProfClass();
    profClass.setId(classObject.getId());
    profClass.setName(classObject.getName());
    profClass.setPackageName(classObject.getPackageName());

    return profClassRepository.save(profClass);
  }

  private static class Context {
    public Cache<String, ProfMethod> profMethodCache = new SimpleMapCache<>();
    public Cache<String, ProfClass> profClassCache = new SimpleMapCache<>();

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

  private static class Param {
    public ProfTreeNode parent;
    public TreeNode node;

    public Param(ProfTreeNode parent, TreeNode node) {
      this.parent = parent;
      this.node = node;
    }
  }
}
