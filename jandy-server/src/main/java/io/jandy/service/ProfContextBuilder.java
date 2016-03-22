package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.java.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;
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

  @Autowired
  private JdbcTemplate jdbc;

  @Autowired
  private TransactionTemplate tt;

  @Async
  public ListenableFuture<Map<String, ProfContextDump>> buildForAsync(Map<String, ProfilingContext> contexts) {
    return tt.execute((st) -> {
      Map<String, ProfContextDump> r = new HashMap<>();
      for (String id : contexts.keySet()) {
        r.put(id, build(contexts.get(id)));
      }
      return new AsyncResult<>(r);
    });
  }

  @Transactional
  public ProfContextDump build(ProfilingContext context) {
//    Map<String, TreeNode> nodes = context.getNodes().stream().collect(Collectors.toMap(TreeNode::getId, Function.identity()));
    Map<String, MethodObject> methods = context.getMethods().stream().collect(Collectors.toMap(MethodObject::getId, Function.identity()));
    Map<String, ClassObject> classes = context.getClasses().stream().collect(Collectors.toMap(ClassObject::getId, Function.identity()));
//    Map<String, ExceptionObject> exceptions = context.getExceptions().stream().collect(Collectors.toMap(ExceptionObject::getId, Function.identity()));

    saveClasses(context.getClasses());
    saveMethods(context.getMethods(), classes);
    saveExceptions(context.getExceptions());

    long t = System.currentTimeMillis();
    logger.trace("==== start building about tree nodes");
    saveTreeNodes(context.getNodes(), methods);
    logger.trace("==== elapsed: {}ms", System.currentTimeMillis() - t);

    ProfContextDump dump = new ProfContextDump();
    logger.trace("rootId:{}, root: {}", context.getRootId(), profTreeNodeRepository.findOne(context.getRootId()));
    dump.setRoot(profTreeNodeRepository.findOne(context.getRootId()));
    dump.setMaxTotalDuration(stream(dump.spliterator(), false).mapToLong(ProfTreeNode::getElapsedTime).max().getAsLong());
    return profContextDumpRepository.save(dump);
  }

  @Transactional
  private int[] saveTreeNodes(List<TreeNode> nodes, Map<String, MethodObject> methods) {
    for (TreeNode n : nodes) {
      if (n.getMethodId() != null) {
        MethodObject mo = methods.get(n.getMethodId());
        n.setMethodId(mo.getId());
      }
    }

    return jdbc.batchUpdate(
        "insert into prof_tree_node(id, method_id, parent_id, concur_thread_name, elapsed_time, root, start_time) values (?, ?, ?, ?, ?, ?, ?)",
        nodes.stream().map((n) -> new Object[]{
            n.getId(),
            n.getMethodId(),
            n.getParentId(),
            n.getAcc() == null ? null : n.getAcc().getConcurThreadName(),
            n.getAcc() == null ? 0 : n.getAcc().getElapsedTime(),
            n.isRoot() ? 1 : 0,
            n.getAcc() == null ? 0 : n.getAcc().getStartTime()
        }).collect(Collectors.toList())
    );
  }

  @Transactional
  private void saveExceptions(List<ExceptionObject> exceptions) {

  }

  @Transactional
  private int[] saveMethods(List<MethodObject> methods, Map<String, ClassObject> classes) {
    methods.stream().forEach((m) -> {
      if (m.getOwnerId() != null) {
        ClassObject co = classes.get(m.getOwnerId());
        m.setOwnerId(co.getId());
      }
    });

    methods = methods.stream().filter((m) -> {
      ProfMethod pm = profMethodRepository.findByNameAndDescriptorAndOwner_Id(m.getName(), m.getDescriptor(), m.getOwnerId());
      if (pm != null) {
        m.setId(pm.getId());
        return false;
      }
      return true;
    }).collect(Collectors.toList());

    return methods.size() > 0 ? jdbc.batchUpdate(
        "insert into prof_method(id, owner_id, access, descriptor, name) values (?, ?, ?, ?, ?)",
        methods.stream().map((m) -> new Object[]{
            m.getId(),
            m.getOwnerId(),
            m.getAccess(),
            m.getDescriptor(),
            m.getName()
        }).collect(Collectors.toList())
    ) : new int[]{};
  }

  @Transactional
  private int[] saveClasses(List<ClassObject> classes) {
    classes = classes.stream().filter((co) -> {
      ProfClass pc = profClassRepository.findByNameAndPackageName(co.getName(), co.getPackageName());
      if (pc != null) {
        co.setId(pc.getId());
        return false;
      }
      return true;
    }).collect(Collectors.toList());

    return classes.size() > 0 ? jdbc.batchUpdate(
        "insert into prof_class(id, name, package_name) values (?, ?, ?)",
        classes.stream().map((c) -> new Object[]{
            c.getId(),
            c.getName(),
            c.getPackageName()
        }).collect(Collectors.toList())
    ) : new int[]{};
  }
}
