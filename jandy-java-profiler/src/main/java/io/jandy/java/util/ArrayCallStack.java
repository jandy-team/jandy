package io.jandy.java.util;

import io.jandy.java.data.TreeNode;

import java.util.Arrays;

/**
 * @author JCooky
 * @since 2016-06-03
 */
public class ArrayCallStack {
  private TreeNode[] elements;
  private int maxSize, size = 0;

  public ArrayCallStack(int size) {
    elements = new TreeNode[size];
    this.maxSize = size;
  }

  public void push(TreeNode e) {
    if (size >= maxSize) {
      elements = Arrays.copyOf(elements, maxSize * 2);
      maxSize *= 2;
    }

    elements[size++] = e;
  }

  public TreeNode pop() {
    if (size == 0)
      throw new NullPointerException();
    return elements[--size];
  }
}
