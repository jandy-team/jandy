package io.jandy.web.util;

import io.jandy.service.TravisClient;
import org.junit.Test;
import org.ocpsoft.prettytime.PrettyTime;

import javax.xml.bind.DatatypeConverter;
import java.util.Calendar;
import java.util.Locale;

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

    System.out.println(result);

    Calendar calendar = DatatypeConverter.parseDateTime((String)result.getBuild().get("started_at"));
    PrettyTime p = new PrettyTime(Locale.ENGLISH);

    System.out.println(p.format(calendar));
    
    assertThat(((Number)result.getBuild().get("commit_id")).longValue(), is(result.getCommit().getId()));
    assertThat(result.getCommit().getId(), is(22542817L));
    assertThat(result.getCommit().getMessage(), is("add depedency of icu4j"));
    assertThat(result.getCommit().getCommitterName(), is("JCooky"));
  }
}

