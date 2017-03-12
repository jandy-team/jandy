package io.jandy.domain.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-03-16
 */
@Data
@Accessors(chain = true)
public class ProfilingContext implements Serializable {
  private long profId;
  private List<ThreadObject> threadObjects;

}
