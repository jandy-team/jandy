package io.jandy.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
public class Build {
  @Id
  @GeneratedValue
  private long id;

  private long number;

  @ManyToOne
  private Branch branch;
}
