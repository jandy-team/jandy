package io.jandy.web.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    assertThat(result.getCommit().getId(), is(22542817L));
    assertThat(result.getCommit().getMessage(), is("add depedency of icu4j"));
  }
}

