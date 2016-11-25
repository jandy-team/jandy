package io.jandy.domain.data;

import com.google.common.base.MoreObjects;

public class BuildInfo {
  private long buildId;
  private long buildNum;
  private String ownerName, repoName;
  private String branchName;
  private String lang;
  private int numSamples;

  public long getBuildId() {
    return buildId;
  }

  public BuildInfo setBuildId(long buildId) {
    this.buildId = buildId;
    return this;
  }

  public long getBuildNum() {
    return buildNum;
  }

  public BuildInfo setBuildNum(long buildNum) {
    this.buildNum = buildNum;
    return this;
  }

  public String getBranchName() {
    return branchName;
  }

  public BuildInfo setBranchName(String branchName) {
    this.branchName = branchName;
    return this;
  }

  public String getLang() {
    return lang;
  }

  public BuildInfo setLang(String lang) {
    this.lang = lang;
    return this;
  }


  public int getNumSamples() {
    return numSamples;
  }

  public BuildInfo setNumSamples(int numSamples) {
    this.numSamples = numSamples;
    return this;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public BuildInfo setOwnerName(String ownerName) {
    this.ownerName = ownerName;
    return this;
  }

  public String getRepoName() {
    return repoName;
  }

  public BuildInfo setRepoName(String repoName) {
    this.repoName = repoName;
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("buildId", buildId)
        .add("buildNum", buildNum)
        .add("ownerName", ownerName)
        .add("repoName", repoName)
        .add("branchName", branchName)
        .add("lang", lang)
        .add("numSamples", numSamples)
        .toString();
  }
}