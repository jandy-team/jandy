package io.jandy.domain.data;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class MethodObject implements Serializable {
  private String id;
  private String name;
  private int access;
  private String descriptor;
  private ClassObject owner;

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

  public void setAccess(int access) {
    this.access = access;
  }

  public int getAccess() {
    return access;
  }

  public void setDescriptor(String descriptor) {
    this.descriptor = descriptor;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public ClassObject getOwner() {
    return owner;
  }

  public MethodObject setOwner(ClassObject owner) {
    this.owner = owner;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodObject that = (MethodObject) o;

    if (access != that.access) return false;
    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (descriptor != null ? !descriptor.equals(that.descriptor) : that.descriptor != null) return false;
    return owner != null ? owner.equals(that.owner) : that.owner == null;

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + access;
    result = 31 * result + (descriptor != null ? descriptor.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    return result;
  }
}
