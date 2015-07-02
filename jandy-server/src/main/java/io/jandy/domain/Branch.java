package io.jandy.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
public class Branch {
  @Id
  @GeneratedValue
  private long id;

  private String name;

  @ManyToOne
  private Project project;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "branch")
  private List<Build> builds = new ArrayList<>();
}
