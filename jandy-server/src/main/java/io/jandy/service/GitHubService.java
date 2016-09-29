package io.jandy.service;

import io.jandy.domain.cache.HttpCacheEntry;
import io.jandy.service.data.GHOrg;
import io.jandy.service.data.GHRepo;
import io.jandy.service.data.GHUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * @author JCooky
 * @since 2015-07-01
 */
@Service
public class GitHubService {
  private Logger logger = LoggerFactory.getLogger(GitHubService.class);

  public final static int PROJECTS_NUMBER_PER_PAGE = 10;

  @Autowired
  private OAuth2ClientContext clientContext;

  @Autowired
  private CacheManager cacheManager;

  public boolean isAnonymous() {
    return clientContext.getAccessToken() == null;
  }

  public GHUser getUser() {
    String url = url("https://api.github.com/user");
    return executeWithCache(url, (restTemplate) -> restTemplate.getForEntity(url, GHUser.class));
  }

  public GHUser getUser(String login) {
    String url = url("https://api.github.com/users/{login}");
    return executeWithCache(url, (restTemplate) -> {
      try {
        return restTemplate.getForEntity(url, GHUser.class, login);
      } catch (HttpClientErrorException e) {
        logger.error(e.getMessage(), e);
        return null;
      }
    });
  }

  public List<GHOrg> getUserOrgs(String login) {
    String url = url("https://api.github.com/users/{login}/orgs");
    return Arrays.asList(executeWithCache(url, (restTemplate) -> restTemplate.getForEntity(url, GHOrg[].class, login)));
  }

  public GHOrg getOrg(String org) {
    String url = url("https://api.github.com/orgs/{org}");
    return executeWithCache(url, (restTemplate) -> restTemplate.getForEntity(url, GHOrg.class, org));
  }

  public List<GHRepo> getUserRepos(String login) {
    String url = url("https://api.github.com/users/{login}/repos");

    String tempUrl = url + "&per_page=" + PROJECTS_NUMBER_PER_PAGE;

    return Arrays.asList(executeWithCache(tempUrl, (restTemplate) -> restTemplate.getForEntity(tempUrl, GHRepo[].class, login)));
  }

  public List<GHRepo> getNextPageUserRepos(String login, int projectCount) {

    String url = url("https://api.github.com/users/{login}/repos");
    int nextPageIndex = (projectCount / PROJECTS_NUMBER_PER_PAGE) + 1;
    String tempUrl = url + "&per_page=" + PROJECTS_NUMBER_PER_PAGE + "&page=" + nextPageIndex;

    return Arrays.asList(executeWithCache(tempUrl, (restTemplate) -> restTemplate.getForEntity(tempUrl, GHRepo[].class, login)));
  }

  public List<GHRepo> getNextPageOrgRepos(String login, int projectCount) {

    String url = url("https://api.github.com/orgs/{login}/repos");
    int nextPageIndex = (projectCount / PROJECTS_NUMBER_PER_PAGE) + 1;
    String tempUrl = url + "&per_page=" + PROJECTS_NUMBER_PER_PAGE + "&page=" + nextPageIndex;

    return Arrays.asList(executeWithCache(tempUrl, (restTemplate) -> restTemplate.getForEntity(tempUrl, GHRepo[].class, login)));
  }

  public List<GHRepo> getOrgRepos(String login) {
    String url = url("https://api.github.com/orgs/{login}/repos");
    return Arrays.asList(executeWithCache(url, (restTemplate) -> restTemplate.getForEntity(url, GHRepo[].class, login)));
  }

  public GHRepo getRepo(String owner, String repo) {
    String url = url("https://api.github.com/repos/{owner}/{repo}");
    return executeWithCache(url, (restTemplate) -> restTemplate.getForEntity(url, GHRepo.class, owner, repo));
  }

  private String url(String url) {
    final String accessToken = clientContext.getAccessToken() != null ? clientContext.getAccessToken().getValue() : null;
    if (accessToken != null)
      return url + "?access_token=" + accessToken;

    return url;
  }

  private <R> R executeWithCache(String key, Function<RestTemplate, ResponseEntity<R>> f) {
    return executeWithCache0(key, f).getBody();
  }

  private <R> HttpCacheEntry<R> executeWithCache0(String key, Function<RestTemplate, ResponseEntity<R>> f) {
    Cache cache = cacheManager.getCache(key);
    HttpCacheEntry<R> entry = cache.get(key, HttpCacheEntry.class);
    RestTemplate restTemplate = createRestTemplate(entry == null ? null : entry.getEtag());
    ResponseEntity<R> httpEntity = f.apply(restTemplate);
    if (entry == null || !HttpStatus.NOT_MODIFIED.equals(httpEntity.getStatusCode())) {
      entry = new HttpCacheEntry<>();
      entry.setEtag(httpEntity.getHeaders().getETag());
      entry.setBody(httpEntity.getBody());
      entry.setHeaders(httpEntity.getHeaders());
      cache.put(key, entry);
    }

    return entry;
  }

  private RestTemplate createRestTemplate(String etag) {
    RestTemplate restTemplate = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = new LinkedList<>();
    interceptors.add((request, body, execution) -> {
      request.getHeaders().add("Accept", "application/vnd.github.v3+json");
      if (etag != null)
        request.getHeaders().add("If-None-Match", etag);

      return execution.execute(request, body);
    });

    restTemplate.setInterceptors(interceptors);

    return restTemplate;
  }
}
