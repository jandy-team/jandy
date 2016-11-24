package io.jandy.java;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import io.jandy.domain.data.TreeNode;
import io.jandy.test.util.WebLog;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author JCooky
 * @since 2016-11-21
 */
@RunWith(Parameterized.class)
public class ProfilerResultTest {
  @Parameterized.Parameters
  public static Iterable<Object> data() {
    return Arrays.asList(new Object[] { "java", "python" });
  }

  @Parameterized.Parameter(0)
  public String language;

  private List<TreeNode> treenodes;

  @Before
  public void setUp() throws Exception {
    treenodes = Lists.newArrayList(new Gson().fromJson(IOUtils.toString(ClassLoader.getSystemResource(language+"-profiler-result/treenodes.json")), TreeNode[].class));
  }

  @After
  public void tearDown() throws Exception {
    treenodes.clear();
    treenodes = null;
  }

  @Test
  public void testCheckElapsedTime() throws IOException {

    treenodes.stream()
        .filter((n) -> n != null && n.getAcc() != null)
        .forEach((n) -> {
          assertThat(n.getAcc(), is(notNullValue()));
          assertThat(n.getAcc().getElapsedTime(), is(greaterThanOrEqualTo(0L)));
        });
  }
}
