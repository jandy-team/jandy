package io.jandy.service.java;

import io.jandy.core.jrat.TraceMetrics;
import io.jandy.domain.Build;
import io.jandy.domain.java.*;
import org.shiftone.jrat.core.Accumulator;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.provider.tree.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FutureAdapter;

import javax.transaction.Transactional;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Service
public class JavaTreeNodeBuilder {
  private static final Logger logger = LoggerFactory.getLogger(JavaProfilingDump.class);

  @Autowired
  private JavaTreeNodeRepository javaTreeNodeRepository;

  @Autowired
  private JavaMethodRepository javaMethodRepository;

  @Autowired
  private JavaClassRepository javaClassRepository;

  @Transactional
  public JavaTreeNode buildTreeNode(TreeNode treeNode, JavaTreeNode parent) {
    JavaTreeNode javaTreeNode = new JavaTreeNode();

    Accumulator accumulator = treeNode.getAccumulator();
    javaTreeNode.setConcurrentThreads(accumulator.getConcurrentThreads());
    javaTreeNode.setMaxConcurrentThreads(accumulator.getMaxConcurrentThreads());
    javaTreeNode.setMaxDuration(accumulator.getMaxDuration());
    javaTreeNode.setMinDuration(accumulator.getMinDuration());
    javaTreeNode.setSumOfSquares(accumulator.getSumOfSquares());
    javaTreeNode.setTotalDuration(accumulator.getTotalDuration());
    javaTreeNode.setTotalEnters(accumulator.getTotalEnters());
    javaTreeNode.setTotalErrors(accumulator.getTotalErrors());
    javaTreeNode.setTotalExits(accumulator.getTotalExits());
    javaTreeNode.setJavaMethod(treeNode.getMethodKey() == null ? null : getJavaMethod(treeNode.getMethodKey()));
    javaTreeNode.setParent(parent);

    javaTreeNode = javaTreeNodeRepository.save(javaTreeNode);

    logger.trace("Building java tree node: {}", javaTreeNode);

    for (TreeNode childNode : treeNode.getChildren()) {
      javaTreeNode.getChildren().add(buildTreeNode(childNode, javaTreeNode));
    }

    return javaTreeNode;
  }

  private JavaMethod getJavaMethod(MethodKey methodKey) {
    JavaClass javaClass = getJavaClass(methodKey.getClassName(), methodKey.getPackageName());
    JavaMethod javaMethod = javaMethodRepository.findByMethodNameAndSignatureAndJavaClass_Id(methodKey.getMethodName(), methodKey.getSignature(), javaClass.getId());
    return javaMethod == null ? buildJavaMethod(methodKey, javaClass) : javaMethod;
  }

  @Transactional
  private JavaMethod buildJavaMethod(MethodKey methodKey, JavaClass javaClass) {
    JavaMethod method = new JavaMethod();
    method.setJavaClass(javaClass);
    method.setMethodName(methodKey.getMethodName());
    method.setSignature(methodKey.getSignature());
    method.setJavaClass(javaClass);

    return javaMethodRepository.save(method);
  }

  private JavaClass getJavaClass(String className, String packageName) {
    JavaClass javaClass = javaClassRepository.findByClassNameAndPackageName(className, packageName);
    return javaClass == null ? buildJavaClass(className, packageName) : javaClass;
  }

  @Transactional
  private JavaClass buildJavaClass(String className, String packageName) {
    JavaClass javaClass = new JavaClass();
    javaClass.setClassName(className);
    javaClass.setPackageName(packageName);

    return javaClassRepository.save(javaClass);
  }
}
