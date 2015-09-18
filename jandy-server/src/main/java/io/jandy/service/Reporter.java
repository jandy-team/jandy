package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import io.jandy.domain.Build;
import io.jandy.domain.User;
import io.jandy.util.SmallTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;

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

  @Async
  public void sendMail(User user, long elapsedTotalDuration, Build current, Build prev) throws MessagingException {
    mailSender.send(msg -> {
      MimeMessageHelper h = new MimeMessageHelper(msg, false, "UTF-8");
      h.setFrom("no-reply@jandy.io");
      h.setTo(user.getEmail());
      h.setSubject("Jandy Performance Report");
      h.setText(FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerConfigurer.getConfiguration().getTemplate("email.ftl"),
          ImmutableMap.of(
              "root", "http://jandy.io",
              "number", current.getNumber(),
              "prevNumber", prev.getNumber(),
              "elapsedTime", SmallTime.format(elapsedTotalDuration).toString())
      ), true);
    });
  }
}
