package io.jandy.web.api;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.*;
import io.jandy.domain.data.ClassObject;
import io.jandy.domain.data.MethodObject;
import io.jandy.domain.data.TreeNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author jcooky
 */
@RestController
@RequestMapping("/rest/travis/v2")
public class TravisRestV2Controller {
  private static Logger logger = LoggerFactory.getLogger(TravisRestV2Controller.class);

  @Autowired private ProfContextDumpRepository profContextDumpRepository;
  @Autowired private BuildRepository buildRepository;
  @Autowired private BranchRepository branchRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private SampleRepository sampleRepository;
  @Autowired private ProfTreeNodeRepository profTreeNodeRepository;

  @Autowired private ProfMethodRepository profMethodRepository;
  @Autowired private ProfClassRepository profClassRepository;

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  @Transactional
  public Map<String, ?> createProf(@RequestBody ProfInfo profInfo) {

    String []slug = StringUtils.split(profInfo.getRepoSlug(), '/');
    Project project = projectRepository.findByAccountAndName(slug[0], slug[1]);

    Branch branch = branchRepository.findByNameAndProject_Id(profInfo.getBranchName(), project.getId());
    if (branch == null) {
      branch = new Branch();
      branch.setName(profInfo.getBranchName());
      branch.setProject(project);
      branch = branchRepository.save(branch);
    }

    Sample sample = sampleRepository.findByNameAndProject_Id(profInfo.getSampleName(), project.getId());
    if (sample == null) {
      sample = new Sample();
      sample.setName(profInfo.getSampleName());
      sample.setProject(project);
      sample = sampleRepository.save(sample);
    }

    Build build = buildRepository.findByTravisBuildId(profInfo.getBuildId());
    if (build == null) {
      build = new Build();
      build.setBranch(branch);
      build.setNumber(profInfo.getBuildNum());
      build = buildRepository.save(build);
    }

    sample.getBuilds().add(build);
    sample = sampleRepository.save(sample);
    build.getSamples().add(sample);
    build = buildRepository.save(build);

    ProfContextDump prof = new ProfContextDump();
    prof.setBuild(build);
    prof.setSample(sample);
    prof = profContextDumpRepository.save(prof);

    return ImmutableMap.of("profId", prof.getId());
  }

  @RequestMapping(method = RequestMethod.DELETE)
  @Transactional
  public void saveProf(@RequestBody Map<String, Object> model) {
    long profId = Long.parseLong(model.get("profId").toString());

    ProfContextDump prof = profContextDumpRepository.findOne(profId);

  }

  @RequestMapping(method = RequestMethod.PUT)
  @Transactional
  public void updateTreeNode(@RequestBody TreeNode treeNodeData) {
    ClassObject co = treeNodeData.getMethod().getOwner();
    ProfClass klass = profClassRepository.findByNameAndPackageName(co.getName(), co.getPackageName());
    if (klass == null) {
      klass = new ProfClass();
      klass.setName(co.getName());
      klass.setPackageName(co.getPackageName());
      klass = profClassRepository.save(klass);
    }

    MethodObject mo = treeNodeData.getMethod();
    ProfMethod method = profMethodRepository.findByNameAndDescriptorAndAccessAndOwner_Id(mo.getName(), mo.getDescriptor(), mo.getAccess(), klass.getId());
    if (method == null) {
      method = new ProfMethod();
      method.setName(mo.getName());
      method.setDescriptor(mo.getDescriptor());
      method.setAccess(mo.getAccess());
      method.setOwner(klass);
      method = profMethodRepository.save(method);
    }

    ProfTreeNode parent = null;
    if (treeNodeData.getParentId() != null) {
      parent = profTreeNodeRepository.findOne(treeNodeData.getParentId());
      if (parent == null) {
        parent = new ProfTreeNode();
        parent.setId(treeNodeData.getParentId());
        parent = profTreeNodeRepository.save(parent);
      }
    }

    ProfTreeNode treeNode = profTreeNodeRepository.findOne(treeNodeData.getId());
    if (treeNode == null)
      treeNode = new ProfTreeNode();
    treeNode.setId(treeNodeData.getId());
    treeNode.setMethod(method);
    treeNode.setRoot(treeNodeData.isRoot());
    treeNode.setElapsedTime(treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getElapsedTime());
    treeNode.setStartTime(treeNodeData.getAcc() == null ? 0L : treeNodeData.getAcc().getStartTime());
    treeNode.setConcurThreadName(treeNodeData.getAcc() == null ? null : treeNodeData.getAcc().getConcurThreadName());
    treeNode.setParent(parent);
    treeNode = profTreeNodeRepository.save(treeNode);
  }

  private static class ProfInfo {
    private String repoSlug;
    private String sampleName;
    private long buildId;
    private long buildNum;
    private String branchName;

    public String getRepoSlug() {
      return repoSlug;
    }

    public ProfInfo setRepoSlug(String repoSlug) {
      this.repoSlug = repoSlug;
      return this;
    }

    public String getSampleName() {
      return sampleName;
    }

    public ProfInfo setSampleName(String sampleName) {
      this.sampleName = sampleName;
      return this;
    }

    public long getBuildId() {
      return buildId;
    }

    public ProfInfo setBuildId(long buildId) {
      this.buildId = buildId;
      return this;
    }

    public long getBuildNum() {
      return buildNum;
    }

    public ProfInfo setBuildNum(long buildNum) {
      this.buildNum = buildNum;
      return this;
    }

    public String getBranchName() {
      return branchName;
    }

    public ProfInfo setBranchName(String branchName) {
      this.branchName = branchName;
      return this;
    }
  }
}
