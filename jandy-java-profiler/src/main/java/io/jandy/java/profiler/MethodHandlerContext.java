package io.jandy.java.profiler;

import io.jandy.thrift.java.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class MethodHandlerContext extends ThreadLocal<MethodHandler> {
  private List<TreeNode> roots = new ArrayList<TreeNode>();

  @Override
  protected MethodHandler initialValue() {
    MethodHandler collector = new MethodHandler();
    roots.add(collector.getRoot());
    return collector;
  }

  public List<TreeNode> getRoots() {
    return roots;
  }
}
