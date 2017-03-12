package io.jandy.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-30
 */
@Entity
@Data
@Accessors(chain = true)
@ToString(exclude = {"samples", "branches", "currentBuild", "user"})
public class Project {
  @Id
  @GeneratedValue
  private long id;

  private String account;
  private String name;

  @Column(unique = true)
  private long gitHubId;

  @ManyToOne
  private User user;

  @OneToOne
  private Build currentBuild;

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "project")
  private List<Branch> branches = new ArrayList<>();

  @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "project")
  private List<Sample> samples = new ArrayList<>();
}
