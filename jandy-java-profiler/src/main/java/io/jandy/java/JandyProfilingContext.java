package io.jandy.java;

import io.jandy.java.data.TreeNode;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class JandyProfilingContext {
  private ProfilingLogCollector collector;
  private long profId;

  public void start() {
    String baseUrl = System.getProperty("jandy.base.url") + "/rest/travis";
    String sampleName = System.getProperty("jandy.sample.name");
    String repoSlug = System.getenv("TRAVIS_REPO_SLUG");
    long buildId = Long.parseLong(System.getenv("TRAVIS_BUILD_ID"));
    long buildNum = Long.parseLong(System.getenv("TRAVIS_BUILD_NUMBER"));
    String branchName = System.getenv("TRAVIS_BRANCH");

    collector = new ProfilingLogCollector(baseUrl);
    this.profId = collector.start(sampleName, repoSlug, branchName, buildId, buildNum);
  }

  public void end() {
    collector.end(profId);
  }

  public DataObjectBuilder getBuilder(final long threadId) {
    return new DataObjectBuilder() {
      public void save(TreeNode node) {
        collector.update(node);
      }
    };
  }

  public long getProfId() {
    return profId;
  }
}
