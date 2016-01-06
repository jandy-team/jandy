package io.jandy.domain;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author JCooky
 * @since 2015-11-28
 */
@Entity
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
        .add("commiterName", committerName)
        .add("committerEmail", committerEmail)
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

  public String getCommitterName() {
    return committerName;
  }

  public Commit setCommitterName(String committerName) {
    this.committerName = committerName;
    return this;
  }

  public String getCommitterEmail() {
    return committerEmail;
  }

  public Commit setCommitterEmail(String committerEmail) {
    this.committerEmail = committerEmail;
    return this;
  }

  public String getCommitterAvatarUrl() {
    return committerAvatarUrl;
  }

  public Commit setCommitterAvatarUrl(String committerAvatarUrl) {
    this.committerAvatarUrl = committerAvatarUrl;
    return this;
  }
}
