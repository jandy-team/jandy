package io.jandy.java;

import io.jandy.java.com.google.gson.Gson;
import io.jandy.java.data.TreeNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class ProfilingLogCollector {
  private String baseUrl;
  private Charset UTF_8 = Charset.forName("UTF-8");

  public ProfilingLogCollector(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public Long start(final String sampleName, final String repoSlug, final String branchName,
                     final long buildId, final long buildNum) {
    return wrap(new WrapFunction<Long>() {
      @Override
      public Long call() throws IOException {

        HttpURLConnection conn = null;
        try {
          conn = (HttpURLConnection) new URL(baseUrl).openConnection();
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestMethod("POST");
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);

          OutputStream os = null;
          try {
            os = conn.getOutputStream();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("repoSlug", repoSlug);
            model.put("sampleName", sampleName);
            model.put("branchName", branchName);
            model.put("buildId", buildId);
            model.put("buildNum", buildNum);

            os.write(new Gson().toJson(model).getBytes("UTF-8"));
            os.flush();

          } finally {
            if (os != null)
              os.close();
          }

          InputStream is = null;
          try {
            is = conn.getInputStream();
            Map<String, Object> model = new Gson().fromJson(new InputStreamReader(is, UTF_8), Map.class);
            return ((Double)model.get("profId")).longValue();
          } finally {
            if (is != null)
              is.close();
          }
        } finally {
          if (conn != null)
            conn.disconnect();
        }
      }
    });
  }

  public void end(final long profId) {
    wrap(new WrapFunction<Void>() {
      @Override
      public Void call() throws IOException {
        HttpURLConnection conn = null;
        try {
          conn = (HttpURLConnection) new URL(baseUrl).openConnection();
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestMethod("DELETE");
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);

          OutputStream os = null;
          try {
            os = conn.getOutputStream();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("profId", profId);

            os.write(new Gson().toJson(model).getBytes("UTF-8"));
            os.flush();

          } finally {
            if (os != null)
              os.close();
          }

          conn.getInputStream().close();
        } finally {
          if (conn != null)
            conn.disconnect();
        }

        return null;
      }
    });
  }

  public synchronized void update(final TreeNode node) {
    wrap(new WrapFunction<Void>() {
      @Override
      public Void call() throws IOException {
        HttpURLConnection conn = null;
        try {
          conn = (HttpURLConnection) new URL(baseUrl).openConnection();
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestMethod("PUT");
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);

          OutputStream os = null;
          try {
            os = conn.getOutputStream();

            os.write(new Gson().toJson(node).getBytes("UTF-8"));
            os.flush();

          } finally {
            if (os != null)
              os.close();
          }

          conn.getInputStream().close();
        } finally {
          if (conn != null)
            conn.disconnect();
        }

        return null;
      }
    });
  }

  private <R> R wrap(WrapFunction<R> fn) {
    try {
      return fn.call();
    } catch (IOException e) {
      e.printStackTrace(System.err);
      return null;
    }
  }

  private interface WrapFunction<R> {
    R call() throws IOException;
  }
}
