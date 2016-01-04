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

  private String message;
  private String committedAt;
  private String sha;

  public String getCommittedAt() {
    return committedAt;
  }

  public Commit setCommittedAt(String committedAt) {
    this.committedAt = committedAt;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public Commit setMessage(String message) {
    this.message = message;
    return this;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("message", message)
        .add("committedAt", committedAt)
        .add("sha", sha)
        .toString();
  }

  public long getId() {
    return id;
  }

  public Commit setId(long id) {
    this.id = id;
    return this;
  }

  public String getSha() {
    return sha;
  }

  public Commit setSha(String sha) {
    this.sha = sha;
    return this;
  }
}
