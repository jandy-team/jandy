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
public class Accumulator implements Externalizable {
  private long elapsedTime;
  private long startTime;
  private String concurThreadName;
  private ExceptionKey exceptionKey;

  public long getElapsedTime() {
    return elapsedTime;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public String getConcurThreadName() {
    return concurThreadName;
  }

  public void setConcurThreadName(String concurThreadName) {
    this.concurThreadName = concurThreadName;
  }

  public ExceptionKey getExceptionKey() {
    return exceptionKey;
  }

  public void setExceptionKey(ExceptionKey exceptionKey) {
    this.exceptionKey = exceptionKey;
  }

  public void writeExternal(ObjectOutput o) throws IOException {
    o.writeLong(elapsedTime);
    o.writeLong(startTime);
    IOUtils.writeUTF(o, concurThreadName);
    o.writeObject(exceptionKey);
  }

  public void readExternal(ObjectInput i) throws IOException, ClassNotFoundException {
    elapsedTime = i.readLong();
    startTime = i.readLong();
    concurThreadName = IOUtils.readUTF(i);
    exceptionKey = (ExceptionKey)i.readObject();
  }
}
