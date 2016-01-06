package io.jandy.service;

import io.jandy.domain.*;
import io.jandy.test.AbstractWebAppTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;

import java.util.concurrent.Future;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * @author JCooky
 * @since 2015-09-12
 */
public class ReporterTest extends AbstractWebAppTestCase {
  @InjectMocks
  private Reporter reporter;

  @Mock
  private JavaMailSender mailSender;

  @Autowired
  @Spy
  private FreeMarkerConfigurer configurer;

  private MimeMessage msg = mock(MimeMessage.class);

  @Before
  public void setUp() throws Exception {
    doAnswer(invocation -> {
      Object str = invocation.getArguments()[0];
      System.out.println(str);
      return null;
    }).when(msg).setContent(anyObject(), anyString());

    doAnswer(invocation -> {
      MimeMessagePreparator mmp = (MimeMessagePreparator) invocation.getArguments()[0];
      mmp.prepare(msg);

      return null;
    }).when(mailSender).send(any(MimeMessagePreparator.class));
  }

  @Test
  public void testSendMail() throws Exception {
    User user = new User();
    user.setEmail("bak723@gmail.com");

    Project project = new Project();
    project.setUser(user);
    project.setAccount("jcooky");
    project.setName("jandy");

    Branch branch = new Branch();
    branch.setId(221L);
    branch.setProject(project);

    Build current = new Build();
    current.setNumber(1L);
    current.setTravisBuildId(12L);
    current.setBranch(branch);


    reporter.sendMail(user, current);
  }
}