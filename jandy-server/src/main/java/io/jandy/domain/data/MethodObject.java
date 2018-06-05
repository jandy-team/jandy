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
public class MethodObject implements Serializable {
  private String id;
  private String name;
  private int access;
  private String descriptor;
  private ClassObject owner;

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
