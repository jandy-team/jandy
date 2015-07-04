package io.jandy.exception;

/**
 * @author JCooky
 * @since 2015-07-01
 */
public class NotSignedInException extends IllegalStateException {
  public NotSignedInException() {
    super();
  }

  public NotSignedInException(String message) {
    super(message);
  }
}
