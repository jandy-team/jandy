package io.jandy.exception;

/**
 * @author user1
 * @date 2015-06-30
 */
public class UserNotFoundException extends Exception {
  public UserNotFoundException() {
  }

  public UserNotFoundException(Throwable e) {
    super(e);
  }
}
