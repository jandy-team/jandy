package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
@Data
public class Branch {
  @Id
  @GeneratedValue
  private long id;

  private String name;

  @ManyToOne
  @JsonIgnore
  private Project project;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "branch")
  @OrderBy("number DESC")
  private List<Build> builds = new ArrayList<>();
}
