package io.jandy.domain.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-03-16
 */
@Data
@Accessors(chain = true)
public class ExceptionObject implements Serializable {

  private String id;
  private ClassObject klass;
  private String message;
}
