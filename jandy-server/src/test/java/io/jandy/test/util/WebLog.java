package io.jandy.test.util;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author JCooky
 * @since 2016-11-01
 */
public class WebLog {
  private String url, method;
  private String body;

  public String getUrl() {
    return url;
  }

  public WebLog setUrl(String url) {
    this.url = url;
    return this;
  }

  public String getMethod() {
    return method;
  }

  public WebLog setMethod(String method) {
    this.method = method;
    return this;
  }

  public String getBody() {
    return body;
  }

  public WebLog setBody(String body) {
    this.body = body;
    return this;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("url", url)
        .add("method", method)
        .add("body", body)
        .toString();
  }

  public static List<WebLog> parse(InputStream in, Charset charset) throws IOException {

    Pattern p1 = Pattern.compile("'(.*?)'"), p2 = Pattern.compile("\\[(.*?)\\]");

    List<WebLog> logs = new ArrayList<>();
    for (String t1 : IOUtils.readLines(in, charset)) {
      String token = "";
      int step = 1;

      WebLog log = null;
      for (int i = 0; i < t1.length(); ++i) {
        token += t1.charAt(i);

        if (step == 1 && "Receive".equals(token.trim())) {
          log = new WebLog();
          step++;
          token = "";
        } else if (step == 2 && p1.matcher(token.trim()).matches()) {
          String url = find(token.trim(), p1);
          log.setUrl(url);
          step++;
          token = "";
        } else if (step == 3 && p2.matcher(token.trim()).matches()) {
          String method = find(token.trim(), p2);
          log.setMethod(method);
          step++;
          token = "";
        } else if (step == 4 && "-->".equals(token.trim())) {
          String body = t1.substring(i + 1).trim();
          body = body.replace("\\n", "\n");
          log.setBody(body);
          logs.add(log);

          break;
        }
      }
    }

    return logs;
  }

  public static List<WebLog> parse(File file, Charset charset) throws IOException {
    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
      return parse(is, charset);
    }
  }

  private static String find(String str, Pattern pattern) {
    Matcher matcher = pattern.matcher(str);
    if (matcher.find()) {
      return matcher.group(1);
    }

    return null;
  }
}
