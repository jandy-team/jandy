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
public class ClassObject implements Serializable {
  private String id;
  private String name;
  private String packageName;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ClassObject that = (ClassObject) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(packageName, that.packageName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, packageName);
  }
}
