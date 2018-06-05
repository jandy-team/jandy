package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class Accumulator {
  private long startTime;
  private long elapsedTime = 0;
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
