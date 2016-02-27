package io.jandy.web.api;

import io.jandy.domain.*;
import io.jandy.exception.IllegalBuildNumberException;
import io.jandy.exception.ProjectNotRegisteredException;
import io.jandy.exception.ResourceNotFoundException;
import io.jandy.service.BuildService;
import io.jandy.service.ProfContextBuilder;
import io.jandy.service.Reporter;
import io.jandy.thrift.java.ProfilingContext;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2015-07-06
 */
@RestController
@RequestMapping("/rest/travis")
public class TravisRestController {
  private static final Logger logger = LoggerFactory.getLogger(TravisRestController.class);

  @Autowired
  private TransactionTemplate tt;

  @Autowired
  private ProfContextBuilder profContextBuilder;
  @Autowired
  private BuildService buildService;
  @Autowired
  private BuildRepository buildRepository;
  @Autowired
  private SampleRepository sampleRepository;
  @Autowired
  private ProfContextDumpRepository profContextDumpRepository;
  @Autowired
  private Reporter reporter;
  @Autowired
  private ProjectRepository projectRepository;

  @RequestMapping(method = RequestMethod.POST)
  @Transactional
  public void putResults(@RequestParam String ownerName,
                         @RequestParam String repoName,
                         @RequestParam String branchName,
                         @RequestParam Long buildId,
                         @RequestParam Long buildNum,
                         @RequestParam String language,
                         @RequestParam("samples") List<MultipartFile> samples) throws IOException, ClassNotFoundException, ProjectNotRegisteredException, TException, MessagingException {
    logger.debug("request /rest/travis with ownerName: {}, repoName: {}, branchName: {}", ownerName, repoName, branchName);

    Project project = projectRepository.findByAccountAndName(ownerName, repoName);
    if (project == null)
      throw new ResourceNotFoundException("project(name='"+ownerName+"/"+repoName+"') is not found");

    Map<String, ProfilingContext> contexts = new HashMap<>();
    for (MultipartFile file : samples) {
      try (InputStream is = file.getInputStream()) {
        ProfilingContext context = new ProfilingContext();
        context.read(new TJSONProtocol(new TIOStreamTransport(is)));

        contexts.put(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".jandy")), context);
      }
    }

    ListenableFuture<Map<String, ProfContextDump>> post = profContextBuilder.buildForAsync(contexts);

    post.addCallback(profiles -> tt.execute((status) -> {
      Build build = buildRepository.findByTravisBuildId(buildId);
      Branch branch = buildService.getBranch(ownerName, repoName, branchName);
      if (build == null) {
        build = new Build();
        build.setBranch(branch);
        build.setTravisBuildId(buildId);
        build.setLanguage(language);
        build.setNumber(buildNum);
        build = buildRepository.save(build);
      }
      for (String sampleName : profiles.keySet()) {
        Sample sample = sampleRepository.findByNameAndProject_Id(sampleName, project.getId());
        ProfContextDump prof = profiles.get(sampleName),
            prevProf = profContextDumpRepository.findLastProfile(branch.getId(), sampleName);
        long elapsedDuration = 0L;

        if (sample == null) {
          sample = new Sample()
              .setProject(project)
              .setName(sampleName);
        } else {
          if (prevProf != null)
            elapsedDuration = prof.getMaxTotalDuration() - prevProf.getMaxTotalDuration();
        }
        sample.getBuilds().add(build);
        sample.getProfiles().add(prof);
        sample = sampleRepository.save(sample);
        build.getSamples().add(sample);

        prof
            .setBuild(build)
            .setSample(sample)
            .setElapsedDuration(elapsedDuration);
        prof = profContextDumpRepository.save(prof);
        build.getProfiles().add(prof);
      }

      buildService.setBuildInfo(build);
      int cnt = build.getProfiles().size(),
          s = build.getProfiles().stream().map((p) -> p.getElapsedDuration() <= 0 ? 1 : 0).reduce((i1, i2) -> i1+i2).get();
      build.setNumSamples(cnt);
      build.setNumSucceededSamples(s);
      build = buildRepository.save(build);

      project.setCurrentBuild(build);
      projectRepository.save(project);

      logger.info("Save build information, so on");

      return null;
    }), (e) -> {
      throw new RuntimeException(e);
    });

    post.addCallback(profiles -> {
      try {
        Build currentBuild = project.getCurrentBuild();

        reporter.sendMail(currentBuild.getBranch().getProject().getUser(), currentBuild);
      } catch (IllegalBuildNumberException | MessagingException e) {
        throw new RuntimeException(e);
      }

      logger.info("Calculate duration compared to prev build");

    }, (e) -> {
      throw new RuntimeException(e);
    });

    logger.info("FINISH");
  }
}
