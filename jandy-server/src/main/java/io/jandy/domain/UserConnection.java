package io.jandy.domain;

import javax.persistence.*;

/**
 * @author JCooky
 * @since 2015-07-16
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UserConnectionRank", columnNames = {"userId", "providerId", "rank"}))
public class UserConnection {
  @EmbeddedId
  private UserConnectionId id;

  private int rank;
  private String displayName;
  @Column(length = 512)
  private String profileUrl;
  @Column(length = 512)
  private String imageUrl;
  @Column(length = 512, nullable = false)
  private String accessToken;
  @Column(length = 512)
  private String secret;
  @Column(length = 512)
  private String refreshToken;

  private Long expireTime;

  public UserConnectionId getId() {
    return id;
  }

  public UserConnection setId(UserConnectionId id) {
    this.id = id;
    return this;
  }

  public int getRank() {
    return rank;
  }

  public UserConnection setRank(int rank) {
    this.rank = rank;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public UserConnection setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public UserConnection setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
    return this;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public UserConnection setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
    return this;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public UserConnection setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public String getSecret() {
    return secret;
  }

  public UserConnection setSecret(String secret) {
    this.secret = secret;
    return this;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public UserConnection setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public UserConnection setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
    return this;
  }
}