package io.jandy.java.key;

import com.github.jcooky.jaal.common.profile.ClassType;
import com.github.jcooky.jaal.common.profile.MethodType;


/**
 * @author JCooky
 * @since 2015-09-20
 */
public class MethodKey {
  private MethodType methodType;
  private ClassType owner;

  public MethodKey(MethodType methodType, ClassType owner) {
    this.methodType = methodType;
    this.owner = owner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MethodKey methodKey = (MethodKey) o;

    if (methodType != null ? !methodType.equals(methodKey.methodType) : methodKey.methodType != null) return false;
    return !(owner != null ? !owner.equals(methodKey.owner) : methodKey.owner != null);

  }

  @Override
  public int hashCode() {
    int result = methodType != null ? methodType.hashCode() : 0;
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    return result;
  }
}
