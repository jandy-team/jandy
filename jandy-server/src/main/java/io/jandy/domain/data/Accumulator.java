package io.jandy.domain.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-03-16
 */
@Data
public class Accumulator implements Serializable {
  private long startTime;
  private long elapsedTime;
  private ExceptionObject exception;
}
