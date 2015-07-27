package io.jandy.web.api;

import io.jandy.domain.Project;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.domain.UserRepository;
import io.jandy.test.AbstractWebAppTestCase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.RequestResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * @author user1
 * @date 2015-07-08
 */
public class TravisRestControllerTest extends AbstractWebAppTestCase {

  @Autowired
  private TravisRestController controller;

  @Autowired
  private ProjectRepository projectRepository;
  @Autowired
  private UserRepository userRepository;

  @Test
  public void testPutResultsForJava() throws Exception {
    User user = new User();
    user = userRepository.save(user);

    Project project = new Project();
    project.setUser(null);
    project.setName("jandy");
    project.setAccount("jcooky");
    project.setUser(user);
    project = projectRepository.save(project);

    MockMultipartFile multipartFile = new MockMultipartFile("results", "results.jrat", MediaType.APPLICATION_OCTET_STREAM_VALUE, ClassLoader.getSystemResourceAsStream("results.jrat"));

    MockMvcBuilders.standaloneSetup(controller).build()
        .perform(MockMvcRequestBuilders.fileUpload("/rest/travis/java")
                .file(multipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("ownerName", "jcooky")
                .param("repoName", "jandy")
                .param("buildId", "1")
                .param("branchName", "master")
                .param("buildNum", "1")
        ).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andDo(MockMvcResultHandlers.print());
  }
}