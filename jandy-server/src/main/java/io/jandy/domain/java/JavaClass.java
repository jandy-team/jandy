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

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
