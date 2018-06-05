package io.jandy.domain.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-06-30
 */
@Data
@Accessors(chain = true)
public class ThreadObject implements Serializable {
  private long threadId;
  private String threadName;
  private String rootId;
}
