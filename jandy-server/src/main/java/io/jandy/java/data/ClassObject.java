package io.jandy.java.data;

import java.util.Objects;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class ClassObject {
  private String id;
  private String name;
  private String packageName;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassObject that = (ClassObject) o;
    return
        Objects.equals(name, that.name) &&
        Objects.equals(packageName, that.packageName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, packageName);
  }
}
