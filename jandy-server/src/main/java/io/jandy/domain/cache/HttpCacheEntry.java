package io.jandy.domain.cache;

import org.springframework.http.HttpHeaders;

import java.io.Serializable;

/**
 * @author JCooky
 * @since 2016-04-10
 */
public class HttpCacheEntry<T> implements Serializable {
  private String etag;
  private T body;
  private HttpHeaders headers;

  public String getEtag() {
    return etag;
  }

  public HttpCacheEntry setEtag(String etag) {
    this.etag = etag;
    return this;
  }

  public T getBody() {
    return body;
  }

  public HttpCacheEntry setBody(T body) {
    this.body = body;
    return this;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public HttpCacheEntry setHeaders(HttpHeaders headers) {
    this.headers = headers;
    return this;
  }
}
