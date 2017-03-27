package io.jandy.domain;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author JCooky
 * @since 2015-11-28
 */
@Entity
@Data
@Accessors(chain = true)
@ToString(exclude = "committerAvatarUrl")
public class Commit {
  @Id
  private long id;

  private String message;
  private String committedAt;
  private String sha;
  private String committerName;
  private String committerEmail;

  @Transient
  private String committerAvatarUrl;
}
