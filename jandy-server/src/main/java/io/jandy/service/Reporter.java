package io.jandy.service;

import com.google.common.collect.ImmutableMap;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import io.jandy.domain.Build;
import io.jandy.domain.User;
import io.jandy.util.SmallTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.Future;

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
