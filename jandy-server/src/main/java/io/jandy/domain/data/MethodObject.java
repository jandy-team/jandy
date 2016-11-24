package io.jandy.domain.data;

import java.io.Serializable;
import java.util.Objects;

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
    return access == that.access &&
        Objects.equals(name, that.name) &&
        Objects.equals(descriptor, that.descriptor) &&
        Objects.equals(owner, that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, access, descriptor, owner);
  }
}
