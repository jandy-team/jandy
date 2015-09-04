package io.jandy.web.api;

import com.google.common.io.Closer;
import com.mysema.query.support.Expressions;
import io.jandy.domain.Build;
import io.jandy.domain.QBuild;
import io.jandy.domain.QProject;
import io.jandy.domain.java.JavaProfilingDump;
import io.jandy.domain.java.JavaProfilingDumpRepository;
import io.jandy.domain.java.JavaTreeNode;
import io.jandy.domain.java.QJavaProfilingDump;
import io.jandy.exception.IllegalBuildNumberException;
import io.jandy.exception.ProjectNotRegisteredException;
import io.jandy.service.BuildService;
import io.jandy.service.Reporter;
import io.jandy.service.java.JavaTreeNodeBuilder;
import io.jandy.thrift.java.ProfilingMetrics;
import io.jandy.web.service.ReportReasonType;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TIOStreamTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

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
  private JavaTreeNodeBuilder javaTreeNodeBuilder;
  @Autowired
  private BuildService buildService;
  @Autowired
  private JavaProfilingDumpRepository javaProfilingDumpRepository;
  @Autowired
  private Reporter reporter;

  @RequestMapping(value = "/java", method = RequestMethod.POST)
  @Transactional
  public void putResultsForJava(@RequestParam String ownerName,
                                @RequestParam String repoName,
                                @RequestParam String branchName,
                                @RequestParam Long buildId,
                                @RequestParam Long buildNum,
                                @RequestParam("results") MultipartFile results) throws IOException, ClassNotFoundException, ProjectNotRegisteredException, TException, MessagingException {
    logger.debug("request /travis/java with ownerName: {}, repoName: {}, branchName: {}", ownerName, repoName, branchName);

    try (Closer closer = Closer.create()) {
      InputStream ois = closer.register(results.getInputStream());
      ProfilingMetrics metrics = new ProfilingMetrics();
      metrics.read(new TCompactProtocol(new TIOStreamTransport(ois)));

      Build build = buildService.getBuildForTravis(buildId);
      if (build == null) {
        build = buildService.createBuild(ownerName, repoName, branchName, buildId, "java", buildNum);
        logger.trace("create the build: {}", build);
      }

      if (build.getJavaProfilingDump() != null) javaProfilingDumpRepository.delete(build.getJavaProfilingDump());

      JavaProfilingDump dump = new JavaProfilingDump();
      dump.setRoot(javaTreeNodeBuilder.buildTreeNode(metrics.getRoot(), null));
      dump.setBuild(build);
      dump.setMaxTotalDuration(stream(dump.spliterator(), false).mapToLong(JavaTreeNode::getElapsedTime).max().getAsLong());
      dump = javaProfilingDumpRepository.save(dump);
      build.setJavaProfilingDump(dump);

      logger.info("add profiling dump to build: {}", build);

      try {
        Build prevBuild = buildService.getPrev(build);
        reporter.sendMail(build.getBranch().getProject().getUser(), dump.getMaxTotalDuration() - prevBuild.getJavaProfilingDump().getMaxTotalDuration(),
            build,
            prevBuild);
      } catch (IllegalBuildNumberException e) {
        logger.info(e.getMessage(), e);
      }

      logger.info("FINISH", build);
    }
  }
}
