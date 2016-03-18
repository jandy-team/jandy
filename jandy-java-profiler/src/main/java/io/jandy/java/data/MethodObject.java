package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class MethodObject {
  private String id;
  private String name;
  private int access;
  private String descriptor;
  private String ownerId;

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

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getOwnerId() {
    return ownerId;
  }
}
