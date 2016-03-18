package io.jandy.service;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jandy.domain.Commit;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-11-28
 */
public class TravisClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(TravisClient.class);
  private static final String TRAVIS_URI = "https://api.travis-ci.org";

  private Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();

  public Result getBuild(long buildId) throws IOException {
    try (CloseableHttpResponse response = HttpClientBuilder.create()
        .build()
        .execute(RequestBuilder.get()
                .setUri(TRAVIS_URI + "/builds/" + buildId)
                .setHeader("Accept", "application/vnd.travis-ci.2+json")
                .setHeader("User-Agent", "Jandy")
                .build()
        )) {
      HttpEntity entity = response.getEntity();
      Header encoding = entity.getContentEncoding();

      return gson.fromJson(new InputStreamReader(entity.getContent(), encoding == null ? Charsets.UTF_8 : Charset.forName(encoding.getValue())), Result.class);
    }
  }

  public static class Result {
    private Map<String, Object> build = new HashMap<>();
    private Commit commit;

    public Commit getCommit() {
      return commit;
    }

    public Result setCommit(Commit commit) {
      this.commit = commit;
      return this;
    }

    public Map<String, Object> getBuild() {
      return build;
    }

    public Result setBuild(Map<String, Object> build) {
      this.build = build;
      return this;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .add("build", build)
          .add("commit", commit)
          .toString();
    }
  }
}
