package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class ExceptionObject {

  private String id;
  private String classId;
  private String message;

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getClassId() {
    return classId;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
