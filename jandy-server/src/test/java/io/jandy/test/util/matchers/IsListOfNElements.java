package io.jandy.test.util.matchers;

import org.mockito.ArgumentMatcher;

import java.util.List;

public class IsListOfNElements extends ArgumentMatcher<List> {
  private final int n;

  public IsListOfNElements(int n) {
    this.n = n;
  }

  public boolean matches(Object list) {
    return ((List) list).size() == n;
  }
}