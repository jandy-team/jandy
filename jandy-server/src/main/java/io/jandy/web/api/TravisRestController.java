package io.jandy.web.api;

import com.google.common.io.Closer;
import io.jandy.domain.*;
import io.jandy.exception.IllegalBuildNumberException;
import io.jandy.exception.ProjectNotRegisteredException;
import io.jandy.service.BuildService;
import io.jandy.service.Reporter;
import io.jandy.service.ProfContextBuilder;
import io.jandy.thrift.java.ProfilingContext;
import io.jandy.web.util.TravisClient;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;

import static java.util.stream.StreamSupport.stream;

/**
 * @author JCooky
 * @since 2015-07-06
 */
@RestController
@RequestMapping("/rest/travis")
public class TravisRestController {
  private static final Logger logger = LoggerFactory.getLogger(TravisRestController.class);

  @Autowired
  private ProfContextBuilder profContextBuilder;
  @Autowired
  private BuildService buildService;
  @Autowired
  private BuildRepository buildRepository;
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
                         @RequestParam("results") MultipartFile results) throws IOException, ClassNotFoundException, ProjectNotRegisteredException, TException, MessagingException {
    logger.debug("request /travis/java with ownerName: {}, repoName: {}, branchName: {}", ownerName, repoName, branchName);

    try (Closer closer = Closer.create()) {
      InputStream ois = closer.register(results.getInputStream());
      ProfilingContext context = new ProfilingContext();
      context.read(new TJSONProtocol(new TIOStreamTransport(ois)));

      Build build = buildService.getBuildForTravis(buildId);
      if (build == null) {
        build = buildService.createBuild(ownerName, repoName, branchName, buildId, language, buildNum);
        logger.trace("create the build: {}", build);
      }

      if (build.getProfContextDump() != null) profContextDumpRepository.delete(build.getProfContextDump());

      ListenableFuture<ProfContextDump> postProf = profContextBuilder.buildForAsync(context, build);

      logger.info("add profiling dump to build: {}", build);

      postProf.addCallback(prof -> {
        try {
          Build currentBuild = prof.getBuild();
          Build prevBuild = buildService.getPrev(currentBuild);
          long elapsedDuration = prof.getMaxTotalDuration() - prevBuild.getProfContextDump().getMaxTotalDuration();

          prof.setElapsedDuration(elapsedDuration);
          prof = profContextDumpRepository.save(prof);

          reporter.sendMail(currentBuild.getBranch().getProject().getUser(),
              elapsedDuration,
              currentBuild,
              prevBuild);
        } catch (IllegalBuildNumberException | MessagingException e) {
          logger.error(e.getMessage(), e);
        }

        logger.info("Calculate duration compared to prev build");

      }, (e) -> logger.error(e.getMessage(), e));

      postProf.addCallback(prof -> {
        buildService.saveBuildInfo(buildId);

        logger.info("Save build information, so on");

      }, (e) -> logger.error(e.getMessage(), e));

      postProf.addCallback(prof -> {
        Project project = prof.getBuild().getBranch().getProject();
        project.setCurrentBuild(prof.getBuild());

        projectRepository.save(project);
      }, (e) -> logger.error(e.getMessage(), e));

      logger.info("FINISH", build);
    }
  }
}
