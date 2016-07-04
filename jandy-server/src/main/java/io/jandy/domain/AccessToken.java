package io.jandy.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by syjsmk on 6/29/16.
 */

@Entity
public class AccessToken {

    @Id
    private String jandyTokenValue;

    private String accessToken;

    public AccessToken() {
    }

    public AccessToken(String jandyTokenValue, String accessToken) {
        this.jandyTokenValue = jandyTokenValue;
        this.accessToken = accessToken;
    }

    public String getJandyTokenValue() {
        return jandyTokenValue;
    }

    public void setJandyTokenValue(String jandyTokenValue) {
        this.jandyTokenValue = jandyTokenValue;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
