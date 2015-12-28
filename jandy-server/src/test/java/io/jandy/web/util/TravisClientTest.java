package io.jandy.web.util;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author JCooky
 * @since 2015-11-28
 */
public class TravisClientTest {

  @Test
  public void testGetBuild() throws Exception {
    TravisClient client = new TravisClient();

    TravisClient.Result result = client.getBuild(79083233);

    String str = "Result{build={id=7.9083233E7, repository_id=5431030.0, commit_id=2.2542817E7, number=9, event_type=push, pull_request=false, pull_request_title=null, pull_request_number=null, config={language=java, jdk=[oraclejdk7], install=/bin/true, before_script=[wget http://jandy.io/jandy-runner.egg], script=[mvn clean package, python jandy-runner.egg java -jar target/jasypt-test-1.0-SNAPSHOT.jar Password123 \"I am opensource developer\", ls -al], .result=configured, group=stable, dist=precise}, state=failed, started_at=2015-12-05T11:46:40Z, finished_at=2015-12-05T11:47:10Z, duration=30.0, job_ids=[7.9083234E7]}, commit=Commit{id=22542817, commitId=null, message=add depedency of icu4j, committedAt=2015-09-07T07:52:18Z}}";

    assertThat(result.toString(), is(str));
  }
}

