package io.jandy.test.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author JCooky
 * @since 2016-11-19
 */
public class WebLogToJson {
  public static void main(String[] args) throws IOException {
    File inputfile = new File(args[0]);
    File outputDir = new File(args[1]);

//    FileUtils.deleteDirectory(outputDir);

    if (outputDir.exists()) {
      System.out.println(String.format("%s is already existed", outputDir));
      System.exit(1);
    }

    if (!outputDir.mkdir()) {
      System.out.println(String.format("%s is not accessed", outputDir));
      System.exit(1);
    }

    File begin = new File(outputDir, "begin.json"),
        treenodes = new File(outputDir, "treenodes.json"),
        end = new File(outputDir, "end.json");

    List<String> objects = new ArrayList<>();
//    FileUtils.writeStringToFile(treenodes, "[\n", true);
    for (WebLog log : WebLog.parse(inputfile, StandardCharsets.UTF_8)) {
      if ("/rest/travis/prof/create".equals(log.getUrl())) {
        FileUtils.writeStringToFile(begin, log.getBody());
      } else if ("/rest/travis/prof/put".equals(log.getUrl())) {
        String str = log.getBody().trim();
        objects.addAll(Arrays.asList(StringUtils.splitByWholeSeparator(str, "\n")));
      } else if ("/rest/travis/prof/save".equals(log.getUrl())) {
        FileUtils.writeStringToFile(end, log.getBody());
      }
    }
    String result = objects.stream()
        .filter((s) -> s != null)
        .map(String::trim)
        .filter((s) -> !s.isEmpty())
        .reduce((s1, s2) -> s1 + ",\n" + s2)
        .get();
    System.out.println(result);
    FileUtils.writeStringToFile(treenodes, "[\n"+result+"\n]");
//    FileUtils.writeStringToFile(treenodes, "]\n", true);
  }
}
