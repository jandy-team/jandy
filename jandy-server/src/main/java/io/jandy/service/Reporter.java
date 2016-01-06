package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.Build;
import io.jandy.domain.ProjectRepository;
import io.jandy.domain.User;
import io.jandy.util.SmallTime;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author JCooky
 * @since 2015-09-03
 */
@Service
public class Reporter {
  @Autowired
  private JavaMailSender mailSender;
  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Autowired
  private GitHubService github;

  @Async
  public void sendMail(User user, Build current) throws MessagingException {
    mailSender.send(msg -> {
      org.eclipse.egit.github.core.User ghUser = null;
      if (current.getCommit() != null)
        ghUser = github.getUser(current.getCommit().getCommitterName());

      PrettyTime p = new PrettyTime(Locale.ENGLISH);
      if (current.getFinishedAt() != null)
        current.setBuildAt(p.format(DatatypeConverter.parseDateTime(current.getFinishedAt())));

      HashMap<String, Object> model = new HashMap<>();
      model.put("project", current.getBranch().getProject());
      model.put("build", current);
      model.put("committerAvatarUrl", ghUser == null ? null : user.getAvatarUrl());

      MimeMessageHelper h = new MimeMessageHelper(msg, false, "UTF-8");
      h.setFrom("no-reply@jandy.io");
      h.setTo(user.getEmail());
      h.setSubject("Jandy Performance Report");
      h.setText(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate("email.ftl"), model), true);


    });
  }
}
