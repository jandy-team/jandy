package io.jandy.core.jrat;

import java.io.ObjectInputStream;
import java.util.Properties;
import java.util.Set;
import javax.swing.JComponent;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.ViewBuilder;
import org.shiftone.jrat.provider.tree.TreeNode;
import org.shiftone.jrat.provider.tree.ui.TraceTreeNode;
import org.shiftone.jrat.provider.tree.ui.TraceViewBuilder;
import org.shiftone.jrat.provider.tree.ui.TraceViewPanel;
import org.shiftone.jrat.util.Assert;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public class TraceMetrics {

  public TreeNode root;
  public Set<MethodKey> allMethodKeys;
  public long sessionStartMs;
  public long sessionEndMs;
  public Properties systemProperties;
  public String hostName;
  public String hostAddress;

  private Object delegation;

  public TraceMetrics(Object tvb) {
    this.delegation = tvb;
  }

  public TreeNode getRoot() {
    try {
      return (TreeNode)FieldUtils.readField(delegation, "root", true);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public Set<MethodKey> getAllMethodKeys() {
    try {
      return (Set<MethodKey>) FieldUtils.readField(delegation, "allMethodKeys", true);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public long getSessionStartMs() {
    try {
      return (long) FieldUtils.readField(delegation, "sessionStartMs", true);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}

