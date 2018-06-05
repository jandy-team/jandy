package io.jandy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-02-06
 */
@Entity
@Data
@Accessors(chain = true)
@ToString(exclude = {"project", "builds", "profiles"})
public class Sample {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne
  private Project project;

  @ManyToMany
  @OrderBy("number DESC")
  @JsonIgnore
  private List<Build> builds = new ArrayList<>();

  @OneToMany(mappedBy = "sample", cascade = CascadeType.REMOVE)
  private List<ProfContextDump> profiles = new ArrayList<>();

}
