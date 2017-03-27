package io.jandy.domain;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JCooky
 * @since 2015-06-29
 */
@Entity
@Data
@Accessors(chain = true)
@ToString(exclude = {"projects"})
public class User {
  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private long gitHubId;

  private String avatarUrl;
  private String login;
  private int publicRepos;
  private String email;

  @Transient
  private String color;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "user")
  private List<Project> projects = new ArrayList<>();
}
