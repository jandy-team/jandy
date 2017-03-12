package io.jandy.domain.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author JCooky
 * @since 2016-03-16
 */
@Data
@Accessors(chain = true)
public class TreeNode implements Serializable {
  private String id = null;
  private boolean root;
  private MethodObject method;
  private Accumulator acc;
  private String parentId;
  private long profId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TreeNode treeNode = (TreeNode) o;
    return root == treeNode.root &&
        profId == treeNode.profId &&
        Objects.equals(id, treeNode.id) &&
        Objects.equals(method, treeNode.method) &&
        Objects.equals(acc, treeNode.acc) &&
        Objects.equals(parentId, treeNode.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, root, method, acc, parentId, profId);
  }
}
