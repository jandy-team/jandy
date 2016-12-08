package io.jandy.service;

import io.jandy.domain.cache.HttpCacheEntry;
import io.jandy.service.data.GHOrg;
import io.jandy.service.data.GHRepo;
import io.jandy.service.data.GHUser;
import io.jandy.util.CachedRestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JCooky
 * @since 2015-07-01
 */
@Service
public class GitHubService {
  private Logger logger = LoggerFactory.getLogger(GitHubService.class);

  @Autowired
  private OAuth2ClientContext clientContext;

  @Autowired
  private CacheManager cacheManager;

  private CachedRestTemplate restTemplate;

  public GitHubService(RestTemplateBuilder builder) {
    this.restTemplate = builder
        .build(CachedRestTemplate.class);

    this.restTemplate.setAccept("application/vnd.github.v3+json");
  }

  public boolean isAnonymous() {
    return clientContext.getAccessToken() == null;
  }

  public GHUser getUser() {
    return getForObject("https://api.github.com/user", GHUser.class);
  }

  public GHUser getUser(String login) {
    try {
      return getForObject("https://api.github.com/users/{login}", GHUser.class, login);
    } catch (HttpClientErrorException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  public List<GHOrg> getUserOrgs(String login) {
    return Arrays.asList(getForObject("https://api.github.com/users/{login}/orgs", GHOrg[].class, login));
  }

  public GHOrg getOrg(String org) {
    return getForObject("https://api.github.com/orgs/{org}", GHOrg.class, org);
  }

  public List<GHRepo> getUserRepos(String login) {

    return getForAll("https://api.github.com/users/{login}/repos", GHRepo[].class, login);
  }

  public List<GHRepo> getOrgRepos(String login) {
    return Arrays.asList(restTemplate.getForObject("https://api.github.com/orgs/{login}/repos", GHRepo[].class, login));
  }

  public GHRepo getRepo(String owner, String repo) {
    return restTemplate.getForObject("https://api.github.com/repos/{owner}/{repo}", GHRepo.class, owner, repo);
  }

  private URI uri(String url, Object... uriParams) {
    final String accessToken = clientContext.getAccessToken() != null ? clientContext.getAccessToken().getValue() : null;
    if (accessToken != null) {
      url = url + (url.indexOf('?') > 0 ? "&" : "?") + "access_token=" + accessToken;
    }

    return restTemplate.getUriTemplateHandler().expand(url, uriParams);
  }

  private <T> T getForObject(String url, Class<T> cls, Object ...uriParams) {

    return restTemplate.getForObject(
        cacheManager.getCache(namespace()),
        uri(url, uriParams),
        cls
    );
  }

  private String namespace() {
    return this.getClass().getName();
  }

  private <R> List<R> getForAll(String url, Class<R[]> cls, Object ...uriParams) {
    HttpCacheEntry<R[]> entry = restTemplate.getForCacheEntry(
        cacheManager.getCache(namespace()),
        uri(url, uriParams),
        cls
    );

    logger.debug("Call getForAll({}, {})", url, cls);
    List<R> result = new ArrayList<>();
    result.addAll(Arrays.asList(entry.getBody()));

    Pattern pattern = Pattern.compile("<(?<url>[:.\\-/a-z?&=0-9_]+)>;\\s+rel=\"(?<key>\\w+)\"");
    if (entry.getLink() != null && !entry.getLink().isEmpty()) {
      entry.getLink().stream()
          .flatMap(links -> Arrays.stream(StringUtils.split(links, ',')))
          .filter(link -> link != null)
          .map(String::trim)
          .map(pattern::matcher)
          .filter(Matcher::matches)
          .map(m -> new KeyValue<>(m.group("key"), m.group("url")))
          .filter(m -> m.getKey().equals("next"))
          .map(KeyValue::getValue)
          .findFirst()
          .ifPresent((nextUrl) -> {
            logger.trace("PRINT nextUrl: {}", nextUrl);
            result.addAll(getForAll(nextUrl, cls));
          });
    }

    return result;
  }

}
