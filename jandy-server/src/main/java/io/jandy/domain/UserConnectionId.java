package io.jandy.domain;

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
}
