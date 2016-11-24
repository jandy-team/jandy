//package io.jandy.web.api;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jandy.domain.Project;
//import io.jandy.domain.ProjectRepository;
//import io.jandy.domain.User;
//import io.jandy.domain.UserRepository;
//import io.jandy.service.TravisClient;
//import io.jandy.service.TravisRestService;
//import io.jandy.test.util.WebLog;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBeans;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.support.TransactionTemplate;
//
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
//
///**
// * @author JCooky
// * @since 2015-07-08
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TravisRestV2ControllerIntegrationTest {
//
//  @Autowired
//  private TravisRestV2Controller controller;
//
//  @MockBean
//  private TravisRestService travisRestService;
//
//  @MockBean
//  private ObjectMapper objectMapper;
//
//  @Before
//  public void setUp() throws Exception {
//    User user = new User();
//    user = userRepository.save(user);
//
//    Project project = new Project();
//    project.setName("commons-io");
//    project.setAccount("jcooky");
//    project.setUser(user);
//    project = projectRepository.save(project);
//  }
//
//  @After
//  public void tearDown() throws Exception {
//    projectRepository.deleteAll();
//    userRepository.deleteAll();
//  }
//
//  @Test
//  public void testCreateProf() throws Exception {
//
//  }
//
//  @Test
//  public void testPutResultsForJava() throws Exception {
////    when(travisClient.getBuild(anyLong())).thenReturn()
//
//    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    mockMvc.perform(
//        post("/rest/travis/begin")
//            .param("branchName", "master")
//            .param("ownerName", "jcooky")
//            .param("repoName", "commons-io")
//            .param("numSamples", "1")
//            .param("buildId", "1")
//            .param("buildNum", "1")
//            .param("lang", "java")
//    ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//
//    try (InputStream is = ClassLoader.getSystemResourceAsStream("java-profiler-result.txt")) {
//      List<WebLog> logs = WebLog.parse(is, StandardCharsets.UTF_8);
//
//      for (WebLog log : logs) {
//        tt.execute((ts) -> {
//
//          try {
//            mockMvc.perform(
//                request(HttpMethod.resolve(log.getMethod().toUpperCase()), log.getUrl())
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(log.getBody())
//            ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//          } catch (Exception e) {
//            throw new RuntimeException(e);
//          }
//
//          return null;
//        });
//      }
//
//
//    }
//
//    mockMvc.perform(
//        post("/rest/travis/finish")
//            .param("buildId", "1")
//    ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//  }
//
//  @Test
//  public void testPutResultsForPython() throws Exception {
//    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    mockMvc.perform(
//        post("/rest/travis/begin")
//            .param("branchName", "master")
//            .param("ownerName", "jcooky")
//            .param("repoName", "commons-io")
//            .param("numSamples", "1")
//            .param("buildId", "1")
//            .param("buildNum", "1")
//            .param("lang", "python")
//    ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//
//    try (InputStream is = ClassLoader.getSystemResourceAsStream("python-profiler-result.txt")) {
//      List<WebLog> logs = WebLog.parse(is, StandardCharsets.UTF_8);
//      for (WebLog log : logs) {
//        mockMvc.perform(
//            request(HttpMethod.resolve(log.getMethod().toUpperCase()), log.getUrl())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(log.getBody())
//        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//      }
//    }
//
//    mockMvc.perform(
//        post("/rest/travis/finish")
//            .param("buildId", "1")
//    ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
//  }
//}