package io.jandy.web.util;

import org.junit.Test;

/**
 * @author JCooky
 * @since 2015-11-28
 */
public class TravisClientTest {

  @Test
  public void testGetBuilds() throws Exception {
    TravisClient client = new TravisClient();

    TravisClient.Result result = client.getBuild(79083233);
    System.out.println(result);
  }
}

