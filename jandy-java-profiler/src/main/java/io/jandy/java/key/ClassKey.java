package io.jandy.java.key;

import com.github.jcooky.jaal.common.profile.ClassType;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public class ClassKey {
  private ClassType classType;

  public ClassKey(ClassType classType) {
    this.classType = classType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClassKey classKey = (ClassKey) o;

    return !(classType != null ? !classType.equals(classKey.classType) : classKey.classType != null);

  }

  @Override
  public int hashCode() {
    return classType != null ? classType.hashCode() : 0;
  }
}
