package io.jandy.domain.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author jcooky
 */
@Data
@Accessors(chain = true)
public class ProfilingInfo implements Serializable {
  private long buildId;
  private String sampleName;
}
