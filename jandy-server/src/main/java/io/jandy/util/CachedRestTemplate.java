package io.jandy.util;

import io.jandy.domain.cache.HttpCacheEntry;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.web.client.*;

import java.net.URI;

/**
 * @author JCooky
 * @since 2016-11-28
 */
public class CachedRestTemplate extends RestTemplate {

  private String accept;

  public void setAccept(String accept) {
    this.accept = accept;
  }

  public <T> T getForObject(Cache cache, URI url, Class<T> responseType) throws RestClientException {
    return getForCacheEntry(cache, url, responseType).getBody();
  }

  public <T> HttpCacheEntry<T> getForCacheEntry(Cache cache, URI url, Class<T> responseType) throws RestClientException {
    HttpCacheEntry<T> entry = cache.get(url.toString(), HttpCacheEntry.class);

    HttpHeaders headers = new HttpHeaders();
    if (accept != null)
      headers.set(HttpHeaders.ACCEPT, accept);
    if (entry != null && entry.getEtag() != null)
      headers.set(HttpHeaders.IF_NONE_MATCH, entry.getEtag());

    RequestCallback requestCallback = httpEntityCallback(new HttpEntity(headers), responseType);
    ResponseExtractor<ResponseEntity<T>> responseExtractor = responseEntityExtractor(responseType);

    ResponseEntity<T> responseEntity = execute(url, HttpMethod.GET, requestCallback, responseExtractor);

    if (entry == null || !HttpStatus.NOT_MODIFIED.equals(responseEntity.getStatusCode())) {
      entry = new HttpCacheEntry<>();
      entry.setEtag(responseEntity.getHeaders().getETag());
      entry.setBody(responseEntity.getBody());
      entry.setLink(responseEntity.getHeaders().get("Link"));
      cache.put(responseEntity, entry);
    }

    return entry;
  }


}
