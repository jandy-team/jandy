package io.jandy.domain.data;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class Accumulator implements Serializable {
  private long startTime;
  private long elapsedTime;
  private ExceptionObject exception;

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getStartTime() {
    return startTime;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public void setException(ExceptionObject exception) {
    this.exception = exception;
  }

  public ExceptionObject getException() {
    return exception;
  }

}
