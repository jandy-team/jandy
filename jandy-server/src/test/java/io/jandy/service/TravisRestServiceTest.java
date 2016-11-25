package io.jandy.service;

import com.google.gson.Gson;
import io.jandy.domain.*;
import io.jandy.domain.data.BuildInfo;
import io.jandy.domain.data.ProfilingContext;
import io.jandy.domain.data.ProfilingInfo;
import io.jandy.domain.data.TreeNode;
import io.jandy.web.api.TravisRestV2Controller;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author JCooky
 * @since 2016-11-19
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TravisRestService.class)
public class TravisRestServiceTest {

  @Autowired
  private TravisRestService travisRestService;

  @MockBean
  private ProfContextDumpRepository profContextDumpRepository;

  @MockBean
  private ProjectRepository projectRepository;

  @MockBean
  private BranchRepository branchRepository;

  @MockBean
  private BuildRepository buildRepository;

  @MockBean
  private ProfilingDaemonService profilingDaemonService;

  @MockBean
  private SampleRepository sampleRepository;

  private BuildInfo bi;
  private Project project;
  private Branch branch;

  @Before
  public void setUp() throws Exception {
    bi = new BuildInfo();
    bi.setLang("java");
    bi.setBranchName("master");
    bi.setBuildNum(1);
    bi.setBuildId(1);
    bi.setNumSamples(5);
    bi.setOwnerName("jcooky");
    bi.setRepoName("jandy");

    project = new Project();
    project.setId(1L);
    project.setAccount(bi.getOwnerName());
    project.setName(bi.getRepoName());

    branch = new Branch();
    branch.setId(2L);
    branch.setName(bi.getBranchName());
    branch.setProject(project);
  }

  @Test
  public void testBegin() throws Exception {

    Build build = new Build();
    build.setId(3L);
    build.setLanguage(bi.getLang());
    build.setTravisBuildId(bi.getBuildId());
    build.setNumber(bi.getBuildNum());
    build.setBranch(branch);
    build.setNumSamples(bi.getNumSamples());

    when(projectRepository.findByAccountAndName(anyString(), anyString())).thenReturn(project);
//    when(branchRepository.findByNameAndProject_Id(anyString(), anyLong())).thenReturn(null);
    when(branchRepository.save(any(Branch.class))).thenReturn(branch);
//    when(buildRepository.findByTravisBuildId(anyLong())).thenReturn(null);
    when(buildRepository.save(any(Build.class))).thenReturn(build);

    travisRestService.begin(bi);

    verify(profilingDaemonService, times(1)).start(eq(bi.getBuildId()));
    verify(projectRepository, times(1)).findByAccountAndName(eq(bi.getOwnerName()), eq(bi.getRepoName()));
    verify(branchRepository, times(1)).findByNameAndProject_Id(eq(bi.getBranchName()), eq(project.getId()));
    verify(buildRepository, times(1)).findByTravisBuildId(eq(bi.getBuildId()));

    verify(branchRepository, times(1)).save(refEq(branch, "id"));
    verify(buildRepository, times(1)).save(refEq(build, "id"));
  }

  @Test
  public void testCreateProf() throws Exception {
    ProfilingInfo profParams = new Gson().fromJson(IOUtils.toString(ClassLoader.getSystemResource("java-profiler-result/begin.json")), ProfilingInfo.class);

    Build build = new Build();
    build.setId(3L);
    build.setLanguage(bi.getLang());
    build.setTravisBuildId(profParams.getBuildId());
    build.setNumber(bi.getBuildNum());
    build.setBranch(branch);
    build.setNumSamples(bi.getNumSamples());

    Sample sample = new Sample();
    sample.setId(4L);
    sample.getBuilds().add(build);

    build.getSamples().add(sample);

    ProfContextDump prof = new ProfContextDump();
    prof.setId(5L);
    prof.setBuild(build);
    prof.setSample(sample);
    prof.setState(ProfContextState.CREATED);

    when(buildRepository.findByTravisBuildId(eq(profParams.getBuildId()))).thenReturn(build);
    when(projectRepository.findByBuild(any(Build.class))).thenReturn(project);
//    Sample sample = sampleRepository.findByNameAndProject_Id(profParams.getSampleName(), project.getId());
    when(sampleRepository.save(any(Sample.class))).thenReturn(sample);
    when(buildRepository.save(any(Build.class))).thenReturn(build);
    when(profContextDumpRepository.save(any(ProfContextDump.class))).thenReturn(prof);

    Map<String, ?> result = travisRestService.createProf(profParams);
    assertEquals(result.get("profId"), prof.getId());

    verify(buildRepository, times(1)).findByTravisBuildId(eq(profParams.getBuildId()));
    verify(projectRepository, times(1)).findByBuild(refEq(build, "id"));
    verify(sampleRepository, times(1)).save(refEq(sample));
    verify(sampleRepository, times(1)).findByNameAndProject_Id(eq(profParams.getSampleName()), eq(profParams.getBuildId()));
    verify(buildRepository, times(1)).save(refEq(build, "id"));
    verify(profContextDumpRepository, times(1)).save(refEq(prof, "id"));
  }

  @Test
  public void testSaveProf() throws Exception {
    ProfilingContext profContext = new Gson().fromJson(IOUtils.toString(ClassLoader.getSystemResource("java-profiler-result/end.json")), ProfilingContext.class);

    Build build = new Build();
    build.setId(3L);
    build.setLanguage(bi.getLang());
    build.setTravisBuildId(bi.getBuildId());
    build.setNumber(bi.getBuildNum());
    build.setBranch(branch);
    build.setNumSamples(bi.getNumSamples());

    ProfContextDump prof = new ProfContextDump();
    prof.setId(profContext.getProfId());
    prof.setBuild(build);
    prof.setState(ProfContextState.CREATED);

    when(profContextDumpRepository.findOne(anyLong())).thenReturn(prof);

    this.travisRestService.saveProf(profContext);

    verify(profContextDumpRepository, times(1)).findOne(eq(profContext.getProfId()));
    verify(profilingDaemonService, times(1)).put(eq(build.getTravisBuildId()), eq(ProfilingDaemonService.Task.SAVE), refEq(profContext));
  }

  @Test
  public void testUpdateTreeNodes() throws Exception {
    ProfilingContext profContext = new Gson().fromJson(IOUtils.toString(ClassLoader.getSystemResource("java-profiler-result/end.json")), ProfilingContext.class);
    List<TreeNode> treenodes = Arrays.asList(new Gson().fromJson(IOUtils.toString(ClassLoader.getSystemResource("java-profiler-result/treenodes.json")), TreeNode[].class));

    Build build = new Build();
    build.setId(3L);
    build.setLanguage(bi.getLang());
    build.setTravisBuildId(bi.getBuildId());
    build.setNumber(bi.getBuildNum());
    build.setBranch(branch);
    build.setNumSamples(bi.getNumSamples());

    ProfContextDump prof = new ProfContextDump();
    prof.setId(profContext.getProfId());
    prof.setBuild(build);
    prof.setState(ProfContextState.CREATED);

    when(profContextDumpRepository.findOne(anyLong())).thenReturn(prof);

    travisRestService.updateTreeNodes(treenodes);

    verify(profContextDumpRepository, times(1)).findOne(eq(prof.getId()));
    verify(profilingDaemonService, times(1)).put(eq(build.getTravisBuildId()), eq(ProfilingDaemonService.Task.UPDATE), refEq(treenodes));
  }

  @Test
  public void testFinish() throws Exception {
    travisRestService.finish(1L);

    verify(profilingDaemonService, times(1)).put(eq(1L), eq(ProfilingDaemonService.Task.FINISH), eq(Long.valueOf(1L)));
  }

}