package io.jandy.domain.cache;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-10
 */
public class HttpCacheEntry implements Serializable {
  private String etag;
  private Object body;

  public String getEtag() {
    return etag;
  }

  public HttpCacheEntry setEtag(String etag) {
    this.etag = etag;
    return this;
  }

  public Object getBody() {
    return body;
  }

  public HttpCacheEntry setBody(Object body) {
    this.body = body;
    return this;
  }
}
