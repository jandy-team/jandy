package io.jandy.domain.java;

import io.jandy.domain.Build;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class JavaProfilingDump {
  @Id
  @GeneratedValue
  private long id;

  @OneToOne
  private JavaTreeNode root;

  @OneToOne
  private Build build;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public JavaTreeNode getRoot() {
    return root;
  }

  public void setRoot(JavaTreeNode root) {
    this.root = root;
  }

  public void setBuild(Build build) {
    this.build = build;
  }

  public Build getBuild() {
    return build;
  }
}
