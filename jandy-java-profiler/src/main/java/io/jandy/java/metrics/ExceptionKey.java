package io.jandy.java.metrics;

import io.jandy.java.utils.IOUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author JCooky
 * @since 2015-08-17
 */
public class ExceptionKey implements Externalizable {
  private ClassKey classKey;
  private ExceptionKey causedBy;
  private String message;

  public ClassKey getClassKey() {
    return classKey;
  }

  public void setClassKey(ClassKey classKey) {
    this.classKey = classKey;
  }

  public ExceptionKey getCausedBy() {
    return causedBy;
  }

  public void setCausedBy(ExceptionKey causedBy) {
    this.causedBy = causedBy;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void writeExternal(ObjectOutput o) throws IOException {
    o.writeObject(classKey);
    o.writeObject(causedBy);
    IOUtils.writeUTF(o, message);
  }

  public void readExternal(ObjectInput i) throws IOException, ClassNotFoundException {
    classKey = (ClassKey)i.readObject();
    causedBy = (ExceptionKey)i.readObject();
    message = IOUtils.readUTF(i);
  }
}
