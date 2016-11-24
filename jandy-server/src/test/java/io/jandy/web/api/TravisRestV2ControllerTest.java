package io.jandy.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.domain.data.TreeNode;
import io.jandy.service.TravisClient;
import io.jandy.service.TravisRestService;
import io.jandy.test.util.WebLog;
import io.jandy.test.util.matchers.IsListOfNElements;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hsqldb.lib.StringInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author JCooky
 * @since 2015-07-08
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TravisRestV2Controller.class)
public class TravisRestV2ControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TravisRestService travisRestService;

  @Test
  public void testUpdateTreeNodeForJava() throws Exception {
    List<TreeNode> treeNodes;
    try (InputStream is = ClassLoader.getSystemResourceAsStream("java-profiler-result/treenodes.json")) {
      treeNodes = Arrays.asList(new Gson().fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), TreeNode[].class));
    }

    List<String> inputs = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < treeNodes.size(); i += 4) {
      sb.setLength(0);
      try {
        sb.append(new Gson().toJson(treeNodes.get(i))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 1))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 2))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 3))).append('\n');
      } catch (IndexOutOfBoundsException ignored) {}
      inputs.add(sb.toString());
    }

    for (String in : inputs) {
      this.mvc.perform(post("/rest/travis/prof/put")
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .content(in))
          .andExpect(status().isOk());
    }

    Thread.sleep(10);
    verify(travisRestService, times(inputs.size()-1)).updateTreeNodes(argThat(new IsListOfNElements(4)));
  }

  @Test
  public void testUpdateTreeNodeForPython() throws Exception {
    List<TreeNode> treeNodes;
    try (InputStream is = ClassLoader.getSystemResourceAsStream("python-profiler-result/treenodes.json")) {
      treeNodes = Arrays.asList(new Gson().fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), TreeNode[].class));
    }

    List<String> inputs = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < treeNodes.size(); i += 4) {
      sb.setLength(0);
      try {
        sb.append(new Gson().toJson(treeNodes.get(i))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 1))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 2))).append('\n');
        sb.append(new Gson().toJson(treeNodes.get(i + 3))).append('\n');
      } catch (IndexOutOfBoundsException ignored) {}
      inputs.add(sb.toString());
    }

    for (String in : inputs) {
      this.mvc.perform(post("/rest/travis/prof/put")
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .content(in))
          .andExpect(status().isOk());
    }

    Thread.sleep(10);
    verify(travisRestService, times(inputs.size())).updateTreeNodes(argThat(new IsListOfNElements(4)));
  }
}