package io.jandy.domain.data;

public class BuildInfo {
    private long buildId;
    private long buildNum;
    private String repoSlug;
    private String branchName;
    private String lang;
    private String sampleName;

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

    public String getRepoSlug() {
      return repoSlug;
    }

    public BuildInfo setRepoSlug(String repoSlug) {
      this.repoSlug = repoSlug;
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

    public String getSampleName() {
      return sampleName;
    }

    public BuildInfo setSampleName(String sampleName) {
      this.sampleName = sampleName;
      return this;
    }
  }