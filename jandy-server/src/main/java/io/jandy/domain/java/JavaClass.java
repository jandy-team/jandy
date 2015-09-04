package io.jandy.domain.java;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class JavaClass {
  @Id
  @GeneratedValue
  private long id;

  private String packageName;
  private String className;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "javaClass")
  private List<JavaMethod> methods;

  public long getId() {
    return id;
  }

  public JavaClass setId(long id) {
    this.id = id;
    return this;
  }

  public String getPackageName() {
    return packageName;
  }

  public JavaClass setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  public String getClassName() {
    return className;
  }

  public JavaClass setClassName(String className) {
    this.className = className;
    return this;
  }

  public List<JavaMethod> getMethods() {
    return methods;
  }

  public JavaClass setMethods(List<JavaMethod> methods) {
    this.methods = methods;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("className", className)
        .append("packageName", packageName)
        .append("id", id)
        .toString();
  }
}
