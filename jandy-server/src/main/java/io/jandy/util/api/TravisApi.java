package io.jandy.util.api;

import com.google.common.base.Objects;
import io.jandy.domain.Commit;
import io.jandy.util.api.json.TResult;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-11-28
 */
@Component
public class TravisApi {
  private static final String TRAVIS_URI = "https://api.travis-ci.org";

  private RestTemplate restTemplate;

  public TravisApi(RestTemplateBuilder builder) {
    this.restTemplate = builder
            .additionalInterceptors((httpRequest, bytes, clientHttpRequestExecution) -> {
              httpRequest.getHeaders().set("Accept", "application/vnd.travis-ci.2+json");
              httpRequest.getHeaders().set("User-Agent", "Jandy");
              return clientHttpRequestExecution.execute(httpRequest, bytes);
            })
            .build();
  }

  public TResult getBuild(long buildId) throws IOException {
    return restTemplate.getForObject(TRAVIS_URI + "/builds/" + buildId, TResult.class);
  }
}
