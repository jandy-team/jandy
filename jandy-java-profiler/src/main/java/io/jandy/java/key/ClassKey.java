package io.jandy.java.key;

/**
 * @author JCooky
 * @since 2015-09-20
 */
public class ClassKey {
  public String packageName, className;

  public ClassKey(String className) {
    int idx = className.lastIndexOf('.');
    String packageName;
    if (idx == -1)
      packageName = "";
    else {
      packageName = className.substring(0, idx);
      className = className.substring(idx+1);
    }

    this.packageName = packageName;
    this.className = className;
  }

  public ClassKey(String packageName, String className) {
    this.packageName = packageName;
    this.className = className;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClassKey classKey = (ClassKey) o;

    if (packageName != null ? !packageName.equals(classKey.packageName) : classKey.packageName != null) return false;
    return className != null ? className.equals(classKey.className) : classKey.className == null;

  }

  @Override
  public int hashCode() {
    int result = packageName != null ? packageName.hashCode() : 0;
    result = 31 * result + (className != null ? className.hashCode() : 0);
    return result;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getClassName() {
    return className;
  }
}
