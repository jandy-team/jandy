package io.jandy.web.api;

import io.jandy.core.jrat.TraceMetrics;
import io.jandy.domain.BranchRepository;
import io.jandy.domain.Build;
import io.jandy.domain.ProjectRepository;
import io.jandy.exception.ProjectNotRegisteredException;
import io.jandy.service.BuildService;
import io.jandy.service.JavaProfilingDumpBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

/**
 * @author JCooky
 * @since 2015-07-06
 */
@RestController
@RequestMapping("/travis")
public class TravisRestController {
  private static final Logger logger = LoggerFactory.getLogger(TravisRestController.class);

  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private BranchRepository branchRepository;
  @Autowired
  private JavaProfilingDumpBuilder javaProfilingDumpBuilder;
  @Autowired
  private BuildService buildService;

  @RequestMapping(value = "/java", method = RequestMethod.POST)
  public void putResultsForJava(@RequestParam String ownerName,
                                @RequestParam String repoName,
                                @RequestParam String branchName,
                                @RequestParam Long buildId,
                                @RequestParam("results") MultipartFile results) throws IOException, ClassNotFoundException, ProjectNotRegisteredException {
    logger.debug("request /travis/java with ownerName: {}, repoName: {}, branchName: {}", ownerName, repoName, branchName);


    ObjectInputStream ois = new ObjectInputStream(new GZIPInputStream(results.getInputStream()));
    TraceMetrics traceMetrics = new TraceMetrics(ois.readObject());

    Build build = buildService.getBuild(ownerName, repoName, branchName, buildId, "java");
    javaProfilingDumpBuilder.build(build, traceMetrics);

    logger.info("add profiling dump to build: {}", build);
  }
}
