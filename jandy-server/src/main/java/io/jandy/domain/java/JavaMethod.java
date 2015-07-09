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
  private String signature = null;

  @ManyToOne
  private JavaClass javaClass;

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

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
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
        .append("signature", signature)
        .append("javaClass", javaClass)
        .toString();
  }
}
