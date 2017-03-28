package io.jandy.util.api;

import io.jandy.util.api.TravisApi;
import io.jandy.util.api.json.TResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Locale;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * @author JCooky
 * @since 2015-11-28
 */
@RunWith(SpringRunner.class)
@RestClientTest(TravisApi.class)
public class TravisApiTest {

  @Autowired
  private TravisApi client;

  @Autowired
  private MockRestServiceServer server;

  private String output = "{\"build\":{\"id\":79083233,\"repository_id\":5431030,\"commit_id\":22542817,\"number\":\"9\",\"event_type\":\"push\",\"pull_request\":false,\"pull_request_title\":null,\"pull_request_number\":null,\"config\":{\"language\":\"java\",\"jdk\":[\"oraclejdk7\"],\"install\":\"/bin/true\",\"before_script\":[\"wget http://jandy.io/jandy-runner.egg\"],\"script\":[\"mvn clean package\",\"python jandy-runner.egg java -jar target/jasypt-test-1.0-SNAPSHOT.jar Password123 \\\"I am opensource developer\\\"\",\"ls -al\"],\".result\":\"configured\",\"group\":\"stable\",\"dist\":\"precise\"},\"state\":\"failed\",\"started_at\":\"2015-12-28T03:20:20Z\",\"finished_at\":\"2015-12-28T03:21:16Z\",\"duration\":56,\"job_ids\":[79083234]},\"commit\":{\"id\":22542817,\"sha\":\"70795c31635e24ea5780cce2481cc4cc777a4de7\",\"branch\":\"master\",\"branch_is_default\":true,\"message\":\"add depedency of icu4j\",\"committed_at\":\"2015-09-07T07:52:18Z\",\"author_name\":\"JCooky\",\"author_email\":\"bak723@gmail.com\",\"committer_name\":\"JCooky\",\"committer_email\":\"bak723@gmail.com\",\"compare_url\":\"https://github.com/jcooky/jasypt-test/compare/3e6d56f9f062...70795c31635e\"},\"jobs\":[{\"id\":79083234,\"repository_id\":5431030,\"build_id\":79083233,\"commit_id\":22542817,\"log_id\":55823659,\"state\":\"failed\",\"number\":\"9.1\",\"config\":{\"language\":\"java\",\"jdk\":\"oraclejdk7\",\"install\":\"/bin/true\",\"before_script\":[\"wget http://jandy.io/jandy-runner.egg\"],\"script\":[\"mvn clean package\",\"python jandy-runner.egg java -jar target/jasypt-test-1.0-SNAPSHOT.jar Password123 \\\"I am opensource developer\\\"\",\"ls -al\"],\".result\":\"configured\",\"os\":\"linux\"},\"started_at\":\"2015-12-28T03:20:20Z\",\"finished_at\":\"2015-12-28T03:21:16Z\",\"queue\":\"builds.docker\",\"allow_failure\":false,\"tags\":null,\"annotation_ids\":[]}],\"annotations\":[]}";


  @Test
  public void testGetBuild() throws Exception {
    long buildId = 79083233;
    server.expect(requestTo("https://api.travis-ci.org/builds/"+buildId))
            .andExpect(method(HttpMethod.GET))
            .andExpect(header(HttpHeaders.ACCEPT, "application/vnd.travis-ci.2+json"))
            .andRespond(withSuccess(output, MediaType.APPLICATION_JSON_UTF8));

    TResult result = client.getBuild(79083233);

    System.out.println(result);

    Calendar calendar = DatatypeConverter.parseDateTime((String)result.getBuild().getStartedAt());
    PrettyTime p = new PrettyTime(Locale.ENGLISH);

    System.out.println(p.format(calendar));
    
    assertThat(result.getBuild().getCommitId(), is(result.getCommit().getId()));
    assertThat(result.getCommit().getId(), is(22542817L));
    assertThat(result.getCommit().getMessage(), is("add depedency of icu4j"));
    assertThat(result.getCommit().getCommitterName(), is("JCooky"));
  }
}
