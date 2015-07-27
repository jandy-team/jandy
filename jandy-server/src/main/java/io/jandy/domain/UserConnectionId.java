package io.jandy.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author JCooky
 * @since 2015-07-16
 */
@Embeddable
public class UserConnectionId implements Serializable {
  private String userId;
  private String providerId;
  private String providerUserId;

  public UserConnectionId() {
  }

  public UserConnectionId(String userId, String providerId, String providerUserId) {
    this.userId = userId;
    this.providerId = providerId;
    this.providerUserId = providerUserId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
  }

  public String getProviderUserId() {
    return providerUserId;
  }

  public void setProviderUserId(String providerUserId) {
    this.providerUserId = providerUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    UserConnectionId that = (UserConnectionId) o;

    return new EqualsBuilder()
        .append(userId, that.userId)
        .append(providerId, that.providerId)
        .append(providerUserId, that.providerUserId)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(userId)
        .append(providerId)
        .append(providerUserId)
        .toHashCode();
  }
}
