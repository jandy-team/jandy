package io.jandy.domain;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author JCooky
 * @since 2015-11-28
 */
@Entity
public class Commit {
  @Id
  @GeneratedValue
  private long id;

  private String commitId;
  private String message;
  private String committedAt;

  public String getMessage() {
    return message;
  }

  public Commit setMessage(String message) {
    this.message = message;
    return this;
  }

  public String getCommitId() {
    return commitId;
  }

  public Commit setCommitId(String commitId) {
    this.commitId = commitId;
    return this;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("commitId", commitId)
        .add("message", message)
        .add("committedAt", committedAt)
        .toString();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
