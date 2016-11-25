package io.jandy.java.key;


/**
 * @author JCooky
 * @since 2015-09-20
 */
public class MethodKey {
  public String name, descriptor;
  public int access;
  public ClassKey owner;
  private String desc;

  public MethodKey(String name, String descriptor, int access, ClassKey owner) {
    this.name = name;
    this.descriptor = descriptor;
    this.access = access;
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodKey methodKey = (MethodKey) o;

    if (access != methodKey.access) return false;
    if (name != null ? !name.equals(methodKey.name) : methodKey.name != null) return false;
    if (descriptor != null ? !descriptor.equals(methodKey.descriptor) : methodKey.descriptor != null) return false;
    return owner != null ? owner.equals(methodKey.owner) : methodKey.owner == null;

  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (descriptor != null ? descriptor.hashCode() : 0);
    result = 31 * result + access;
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    return result;
  }

  public String getName() {
    return name;
  }

  public int getAccess() {
    return access;
  }

  public String getDesc() {
    return desc;
  }
}
