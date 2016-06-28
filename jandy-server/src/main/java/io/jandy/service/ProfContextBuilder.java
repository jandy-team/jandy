package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.domain.data.*;
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
  @Autowired
  private ProfExceptionRepository profExceptionRepository;

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

    ProfContextDump dump = new ProfContextDump();

    long t = System.currentTimeMillis();
    logger.trace("==== start building about tree nodes");
    List<ProfTreeNode> nodes = saveTreeNodes(context.getTreeNodes(), dump);
    logger.trace("==== elapsed: {}ms", System.currentTimeMillis() - t);


    logger.trace("rootId:{}, root: {}", dump.getRoot().getId(), dump.getRoot());
    dump.setMaxTotalDuration(nodes.stream().mapToLong(ProfTreeNode::getElapsedTime).max().getAsLong());
    return profContextDumpRepository.save(dump);
  }

  @Transactional
  private List<ProfTreeNode> saveTreeNodes(List<TreeNode> nodes, ProfContextDump dump) {
    List<ProfTreeNode> results = new ArrayList<>(nodes.size());
    for (TreeNode treeNode : nodes) {
      ProfTreeNode node = new ProfTreeNode();
      node.setId(treeNode.getId());
      node.setMethod(saveMethod(treeNode.getMethod()));
      node.setElapsedTime(treeNode.getAcc() == null ? 0L : treeNode.getAcc().getElapsedTime());
      node.setStartTime(treeNode.getAcc() == null ? 0L : treeNode.getAcc().getStartTime());
      node.setConcurThreadName(treeNode.getAcc() == null ? null : treeNode.getAcc().getConcurThreadName());
      node.setException(saveException(treeNode.getAcc() == null ? null : treeNode.getAcc().getException()));
      node.setRoot(treeNode.isRoot());
      node.setParent(treeNode.getParentId() == null ? null : profTreeNodeRepository.findOne(treeNode.getParentId()));
      node = profTreeNodeRepository.save(node);

      if (treeNode.isRoot())
        dump.setRoot(node);

      results.add(node);
    }

    return results;
  }

  @Transactional
  private ProfException saveException(ExceptionObject exception) {
    if (exception == null)
      return null;

    ProfException e = new ProfException();
    e.setKlass(saveClass(exception.getKlass()));
    e.setMessage(exception.getMessage());
    return profExceptionRepository.save(e);
  }

  @Transactional
  private ProfMethod saveMethod(MethodObject method) {
    ProfClass owner = saveClass(method.getOwner());
    ProfMethod m = profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(method.getName(), method.getDescriptor(), method.getAccess(), owner.getId());
    if (m == null) {
      m = new ProfMethod();
      m.setName(method.getName());
      m.setAccess(method.getAccess());
      m.setDescriptor(method.getDescriptor());
      m.setOwner(owner);
      m = profMethodRepository.save(m);
    }

    return m;
  }

  @Transactional
  private ProfClass saveClass(ClassObject klass) {
    ProfClass cls = profClassRepository.findByNameAndPackageName(klass.getName(), klass.getPackageName());
    if (cls == null) {
      cls = new ProfClass();
      cls.setPackageName(klass.getPackageName());
      cls.setName(klass.getName());
      cls = profClassRepository.save(cls);
    }
    return cls;
  }
}
