package io.jandy.java.profiler;

import io.jandy.thrift.java.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MethodCollectorThreadLocalFactory extends ThreadLocal<MethodCollector> {
  private List<TreeNode> roots = new ArrayList<TreeNode>();

  @Override
  protected MethodCollector initialValue() {
    MethodCollector collector = new MethodCollector();
    roots.add(collector.getRoot());
    return collector;
  }

  public List<TreeNode> getRoots() {
    return roots;
  }
}
