package io.jandy.domain.cache;

import java.io.Serializable;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-04-10
 */
public class HttpCacheEntry<T> implements Serializable {
  private String etag;
  private T body;
  private List<String> link;

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


  public void setLink(List<String> link) {
    this.link = link;
  }

  public List<String> getLink() {
    return link;
  }
}
