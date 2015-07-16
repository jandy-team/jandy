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

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Long getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(Long expireTime) {
    this.expireTime = expireTime;
  }

  public UserConnectionId getId() {
    return id;
  }

  public void setId(UserConnectionId id) {
    this.id = id;
  }
}

//  create table UserConnection (userId varchar(255) not null,
//  providerId varchar(255) not null,
//  providerUserId varchar(255),
//  rank int not null,
//  displayName varchar(255),
//  profileUrl varchar(512),
//  imageUrl varchar(512),
//  accessToken varchar(512) not null,
//  secret varchar(512),
//  refreshToken varchar(512),
//  expireTime bigint,
//  primary key (userId, providerId, providerUserId));
//  create unique index UserConnectionRank on UserConnection(userId, providerId, rank);
