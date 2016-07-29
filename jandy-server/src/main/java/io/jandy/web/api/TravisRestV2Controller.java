package io.jandy.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jandy.domain.data.*;
import io.jandy.service.TravisRestService;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jcooky
 */
@RestController
@RequestMapping("/rest/travis")
public class TravisRestV2Controller {
  private static final Logger logger = LoggerFactory.getLogger(TravisRestV2Controller.class);

  @Autowired
  private TravisRestService travisRestService;

  @Autowired
  private ObjectMapper objectMapper;

  @RequestMapping(value = "/begin", method = RequestMethod.POST)
  public void begin(BuildInfo buildInfo) {
    travisRestService.begin(buildInfo);
  }

  @RequestMapping(value = "/finish", method = RequestMethod.POST)
  public void finish(@RequestParam long buildId) throws Exception {
    travisRestService.finish(buildId);
  }

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public Map<String, ?> createProf(@RequestBody ProfilingInfo profParams) {
    return travisRestService.createProf(profParams);
  }

  @RequestMapping(value = "/{id:[0-9]+}", method = RequestMethod.POST)
  public void saveProf(@PathVariable long id, @RequestBody ProfilingContext profilingContext) throws Exception {
    travisRestService.saveProf(profilingContext);
  }

  @RequestMapping(method = RequestMethod.PUT)
  public void updateTreeNode(InputStream is) throws Exception {
    List<TreeNode> treeNodes =
    IOUtils.readLines(is, Charsets.UTF_8)
        .stream()
        .map(this::toTreeNode)
        .collect(Collectors.toList());

    travisRestService.updateTreeNodes(treeNodes);
  }

  private TreeNode toTreeNode(String json) {
    try {
      return objectMapper.reader().forType(TreeNode.class).readValue(json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
