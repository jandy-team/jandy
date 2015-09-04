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

  public JavaMethod setId(long id) {
    this.id = id;
    return this;
  }

  public String getMethodName() {
    return methodName;
  }

  public JavaMethod setMethodName(String methodName) {
    this.methodName = methodName;
    return this;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public JavaMethod setDescriptor(String descriptor) {
    this.descriptor = descriptor;
    return this;
  }

  public JavaClass getJavaClass() {
    return javaClass;
  }

  public JavaMethod setJavaClass(JavaClass javaClass) {
    this.javaClass = javaClass;
    return this;
  }

  public int getAccess() {
    return access;
  }

  public JavaMethod setAccess(int access) {
    this.access = access;
    return this;
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
}
