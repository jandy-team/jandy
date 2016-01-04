package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.Branch;
import io.jandy.domain.Build;
import io.jandy.domain.BuildRepository;
import io.jandy.domain.Commit;
import io.jandy.test.AbstractWebAppTestCase;
import io.jandy.web.util.TravisClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author JCooky
 * @since 2015-12-28
 */
public class BuildServiceTest {

  @InjectMocks
  private BuildService buildService;

  @Mock(name = "buildRepository")
  private BuildRepository dbBuild;

  @Mock(name = "travisClient")
  private TravisClient travisClient;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSaveBuildInfo() throws Exception {
    // setup travisClient and buildRepository
    final long buildId = 1L;

    Build build = new Build();
    build.setId(1L);
    build.setBranch(new Branch());
    build.setLanguage("java");
    build.setNumber(99);
    build.setProfContextDump(null);
    build.setTravisBuildId(buildId);

    when(dbBuild.findByTravisBuildId(buildId)).thenReturn(build);
    when(dbBuild.save(build)).thenReturn(build);

    TravisClient.Result result = new TravisClient.Result();
    result.setBuild(
        ImmutableMap.<String, Object>builder()
            .put("number", 9)
            .put("state", "passed")
            .build()
    );
    result.setCommit(
        new Commit()
            .setId(172831L)
    );
    when(travisClient.getBuild(buildId)).thenReturn(result);

    Future<Build> fBuild = buildService.saveBuildInfo(buildId);
    Build b = fBuild.get();

    assertThat(b.getCommit(), is(result.getCommit()));
//    assertThat(b.get)
  }
}

//Result{build={id=7.9083233E7, repository_id=5431030.0, commit_id=2.2542817E7, number=9, event_type=push, pull_request=false, pull_request_title=null, pull_request_number=null, config={language=java, jdk=[oraclejdk7], install=/bin/true, before_script=[wget http://jandy.io/jandy-runner.egg], script=[mvn clean package, python jandy-runner.egg java -jar target/jasypt-test-1.0-SNAPSHOT.jar Password123 \"I am opensource developer\", ls -al], .result=configured, group=stable, dist=precise}, state=failed, started_at=2015-12-05T11:46:40Z, finished_at=2015-12-05T11:47:10Z, duration=30.0, job_ids=[7.9083234E7]}, commit=Commit{id=22542817, commitId=null, message=add depedency of icu4j, committedAt=2015-09-07T07:52:18Z}}"