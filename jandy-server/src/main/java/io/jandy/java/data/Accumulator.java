package io.jandy.java.data;

/**
 * @author JCooky
 * @since 2016-03-16
 */
public class Accumulator {
  private String concurThreadName;
  private long startTime;
  private long elapsedTime;
  private String exceptionId;

  public void setConcurThreadName(String concurThreadName) {
    this.concurThreadName = concurThreadName;
  }

  public String getConcurThreadName() {
    return concurThreadName;
  }

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

  public void setExceptionId(String exceptionId) {
    this.exceptionId = exceptionId;
  }

  public String getExceptionId() {
    return exceptionId;
  }
}
