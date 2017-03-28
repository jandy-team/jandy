package io.jandy.util.api;

import io.jandy.util.api.GitHubApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author JCooky
 * @since 2016-11-27
 */
@RunWith(SpringRunner.class)
@RestClientTest(GitHubApi.class)
public class GitHubApiTest {

  @Autowired
  private GitHubApi gitHubApi;

  @MockBean
  private CacheManager cacheManager;

  @MockBean
  private OAuth2ClientContext clientContext;

  @Autowired
  private MockRestServiceServer server;

  @Test
  public void testGetUserRepos() throws Exception {
    HttpHeaders headers;

    Cache page1 = mock(Cache.class, "page1"),
        page2 = mock(Cache.class, "page2");

    when(cacheManager.getCache(eq(GitHubApi.class.getName()))).thenReturn(page1);
    when(cacheManager.getCache(eq(GitHubApi.class.getName()))).thenReturn(page2);

    headers = new HttpHeaders();
    headers.set(HttpHeaders.ETAG, "abcd");
    headers.set("Link", "<https://api.github.com/user/1345314/repos?page=2>; rel=\"next\", <https://api.github.com/user/1345314/repos?page=2>; rel=\"last\"");

    server.expect(once(), requestTo("https://api.github.com/users/jcooky/repos")).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(new ClassPathResource("github/users-jcooky-repos.1.json"), MediaType.APPLICATION_JSON).headers(headers));

    headers = new HttpHeaders();
    headers.set(HttpHeaders.ETAG, "efgh");
    headers.set("Link", "<https://api.github.com/user/1345314/repos?page=1>; rel=\"first\", <https://api.github.com/user/1345314/repos?page=1>; rel=\"prev\"");

    server.expect(once(), requestTo("https://api.github.com/user/1345314/repos?page=2")).andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess().headers(headers).body(new ClassPathResource("github/users-jcooky-repos.2.json")).contentType(MediaType.APPLICATION_JSON));

    gitHubApi.getUserRepos("jcooky");

    server.verify();
  }
}