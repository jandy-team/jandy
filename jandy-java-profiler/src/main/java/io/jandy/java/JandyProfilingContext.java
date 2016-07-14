package io.jandy.java;

import io.jandy.java.data.ThreadObject;
import io.jandy.java.data.TreeNode;
import io.jandy.java.profiler.ThreadContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class JandyProfilingContext {
  private ProfilingLogCollector collector;
  private long profId;

  public void start() throws IOException {
    String baseUrl = System.getProperty("jandy.base.url") + "/rest/travis";
    String sampleName = System.getProperty("jandy.sample.name");
    long batchSize = toBytes(System.getProperty("jandy.batch.size"));

    String repoSlug = System.getenv("TRAVIS_REPO_SLUG");
    long buildId = Long.parseLong(System.getenv("TRAVIS_BUILD_ID"));
    long buildNum = Long.parseLong(System.getenv("TRAVIS_BUILD_NUMBER"));
    String branchName = System.getenv("TRAVIS_BRANCH");

    collector = new ProfilingLogCollector(baseUrl, batchSize);
    this.profId = collector.start(sampleName, repoSlug, branchName, buildId, buildNum, "java");
  }

  public void end(List<ThreadContext> threadContexts) {
    List<ThreadObject> threadObjects = new ArrayList<ThreadObject>();
    for (ThreadContext tc : threadContexts) {
      threadObjects.add(tc.getThreadObject());
    }
    collector.end(profId, threadObjects);
  }

  public DataObjectBuilder getBuilder() {
    return new DataObjectBuilder() {
      public void save(TreeNode node) {
        collector.update(node);
      }
    };
  }

  public long getProfId() {
    return profId;
  }

  public static long toBytes(String filesize) {
    long returnValue = -1;
    Pattern patt = Pattern.compile("([\\d.]+)([GMK]B)", Pattern.CASE_INSENSITIVE);
    Matcher matcher = patt.matcher(filesize);
    Map<String, Integer> powerMap = new HashMap<String, Integer>();
    powerMap.put("GB", 3);
    powerMap.put("MB", 2);
    powerMap.put("KB", 1);
    if (matcher.find()) {
      String number = matcher.group(1);
      int pow = powerMap.get(matcher.group(2).toUpperCase());
      BigDecimal bytes = new BigDecimal(number);
      bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow));
      returnValue = bytes.longValue();
    }
    return returnValue;
  }
}
