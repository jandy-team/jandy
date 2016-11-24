package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class ExceptionObject {

  private String id;
  private ClassObject klass;
  private String message;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public ClassObject getKlass() {
    return klass;
  }

  public ExceptionObject setKlass(ClassObject klass) {
    this.klass = klass;
    return this;
  }
}
