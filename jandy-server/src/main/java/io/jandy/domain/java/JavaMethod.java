package io.jandy.domain.java;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@Entity
public class JavaMethod {
  @Id
  @GeneratedValue
  private long id;

  private String methodName = null;
  private String descriptor = null;

  @ManyToOne
  private JavaClass javaClass;
  private int access;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(String descriptor) {
    this.descriptor = descriptor;
  }

  public JavaClass getJavaClass() {
    return javaClass;
  }

  public void setJavaClass(JavaClass javaClass) {
    this.javaClass = javaClass;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("methodName", methodName)
        .append("descriptor", descriptor)
        .append("javaClass", javaClass)
        .toString();
  }

  public void setAccess(int access) {
    this.access = access;
  }

  public int getAccess() {
    return access;
  }
}
