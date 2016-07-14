package io.jandy.web.api;

import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.service.TravisClient;
import io.jandy.test.AbstractWebAppTestCase;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JCooky
 * @since 2015-07-08
 */
public class TravisRestV2ControllerTest extends AbstractWebAppTestCase {

  @Autowired
  private TravisRestV2Controller controller;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserRepository userRepository;

  @Mock
  private TravisClient travisClient;

  @Before
  public void setUp() throws Exception {
    User user = new User();
    user = userRepository.save(user);

    Project project = new Project();
    project.setName("jandy");
    project.setAccount("jcooky");
    project.setUser(user);
    project = projectRepository.save(project);
  }

  @After
  public void tearDown() throws Exception {
    projectRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testPutResultsForJava() throws Exception {
//    when(travisClient.getBuild(anyLong())).thenReturn()

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    for (String line : IOUtils.readLines(ClassLoader.getSystemResourceAsStream("result.txt"))) {
      Pattern pattern = Pattern.compile("Receive \\[(.+)\\] --> (.+)");
      Matcher matcher = pattern.matcher(line);
      String data = null;
      HttpMethod method = null;
      if (matcher.find()) {
        String m = matcher.group(1);
        if (m.equalsIgnoreCase("post")) {
          method = HttpMethod.POST;
        } else if (m.equalsIgnoreCase("delete")) {
          method = HttpMethod.DELETE;
        } else if (m.equalsIgnoreCase("put")) {
          method = HttpMethod.PUT;
        }
        data = matcher.group(2);
      }

      mockMvc.perform(MockMvcRequestBuilders.request(method, "/rest/travis/v2")
          .contentType(MediaType.APPLICATION_JSON)
          .content(data)
      )
          .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
          .andDo(MockMvcResultHandlers.log());
    }

//    MockMultipartFile multipartFile = new MockMultipartFile("samples", "java-profiler-result.jandy",
//        MediaType.APPLICATION_OCTET_STREAM_VALUE, ClassLoader.getSystemResourceAsStream("java-profiler-result.jandy"));
//
  }

  @Test
  public void testPutResultsForPython() throws Exception {
//    User user = new User();
//    user = userRepository.save(user);
//
//    Project project = new Project();
//    project.setUser(null);
//    project.setName("jandy");
//    project.setAccount("jcooky");
//    project.setUser(user);
//    project = projectRepository.save(project);

    MockMultipartFile multipartFile = new MockMultipartFile("samples", "python-profiler-result.jandy",
        MediaType.APPLICATION_OCTET_STREAM_VALUE, ClassLoader.getSystemResourceAsStream("python-profiler-result.jandy"));

    MockMvcBuilders.standaloneSetup(controller).build()
        .perform(MockMvcRequestBuilders.fileUpload("/rest/travis")
            .file(multipartFile)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .param("ownerName", "jcooky")
            .param("repoName", "jandy")
            .param("buildId", "1")
            .param("branchName", "master")
            .param("buildNum", "1")
            .param("language", "python")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andDo(MockMvcResultHandlers.print());
  }
}