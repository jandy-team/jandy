package io.jandy.service.java;

import io.jandy.domain.java.*;
import io.jandy.java.metrics.Accumulator;
import io.jandy.java.metrics.ClassKey;
import io.jandy.java.metrics.MethodKey;
import io.jandy.java.metrics.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    javaTreeNode.setElapsedTime(accumulator.getElapsedTime());
    javaTreeNode.setStartTime(accumulator.getStartTime());
    javaTreeNode.setConcurThreadName(accumulator.getConcurThreadName());
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
    JavaClass javaClass = getJavaClass(methodKey.getOwner());
    JavaMethod javaMethod = javaMethodRepository.findByMethodNameAndDescriptorAndJavaClass_Id(methodKey.getName(), methodKey.getDescriptor(), javaClass.getId());
    return javaMethod == null ? buildJavaMethod(methodKey, javaClass) : javaMethod;
  }

  @Transactional
  private JavaMethod buildJavaMethod(MethodKey methodKey, JavaClass javaClass) {
    JavaMethod method = new JavaMethod();
    method.setJavaClass(javaClass);
    method.setMethodName(methodKey.getName());
    method.setDescriptor(methodKey.getDescriptor());
    method.setAccess(methodKey.getAccess());
    method.setJavaClass(javaClass);

    return javaMethodRepository.save(method);
  }

  private JavaClass getJavaClass(ClassKey classKey) {
    JavaClass javaClass = javaClassRepository.findByClassNameAndPackageName(classKey.getName(), classKey.getPackageName());
    return javaClass == null ? buildJavaClass(classKey.getName(), classKey.getPackageName()) : javaClass;
  }

  @Transactional
  private JavaClass buildJavaClass(String className, String packageName) {
    JavaClass javaClass = new JavaClass();
    javaClass.setClassName(className);
    javaClass.setPackageName(packageName);

    return javaClassRepository.save(javaClass);
  }
}
