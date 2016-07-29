package io.jandy.java;

import io.jandy.java.com.google.gson.Gson;
import io.jandy.java.data.ProfilingContext;
import io.jandy.java.data.ThreadObject;
import io.jandy.java.data.TreeNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class ProfilingLogCollector {
  private final long batchSize;
  private String baseUrl;
  private Charset UTF_8 = Charset.forName("UTF-8");

  private File batchFile = null;
  private BufferedOutputStream bos = null;

  public ProfilingLogCollector(String baseUrl, long batchSize) throws IOException {
    this.baseUrl = baseUrl;
    this.batchSize = batchSize;
    this.batchFile = File.createTempFile("batch", Long.toString(System.nanoTime()));
  }

  public Long start(final String sampleName, final String repoSlug, final String branchName,
                    final long buildId, final long buildNum, final String lang) {
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
            model.put("buildId", buildId);
            model.put("sampleName", sampleName);

            os.write(new Gson().toJson(model).getBytes(UTF_8));
            os.flush();

          } finally {
            if (os != null)
              os.close();
          }

          InputStream is = null;
          try {
            is = conn.getInputStream();
            Map<String, Object> model = new Gson().fromJson(new InputStreamReader(is, UTF_8), Map.class);
            return ((Double) model.get("profId")).longValue();
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

  public void end(final long profId, final List<ThreadObject> threadObjects) {
    update();

    wrap(new WrapFunction<Void>() {
      @Override
      public Void call() throws IOException {
        HttpURLConnection conn = null;
        try {
          conn = (HttpURLConnection) new URL(baseUrl+"/"+profId).openConnection();
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestMethod("POST");
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);

          OutputStream os = null;
          try {
            os = conn.getOutputStream();
            ProfilingContext profilingContext = new ProfilingContext();
            profilingContext.setProfId(profId);
            profilingContext.setThreadObjects(threadObjects);

            os.write(new Gson().toJson(profilingContext).getBytes(UTF_8));
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

    batchFile.delete();
  }

  public synchronized void update(final TreeNode node) {
    final byte[] data = (new Gson().toJson(node, TreeNode.class) + '\n').getBytes(UTF_8);

    if (this.batchFile.length() + data.length >= this.batchSize)
      update();

    wrap(new WrapFunction<Void>() {
      @Override
      public Void call() throws IOException {
        if (bos == null) {
          bos = new BufferedOutputStream(new FileOutputStream(batchFile));
        }
        bos.write(data);
        return null;
      }
    });
  }

  private void update() {
    if (bos == null)
      return;

    wrap(new WrapFunction<Void>() {
      @Override
      public Void call() throws IOException {
        bos.close();
        bos = null;

        HttpURLConnection conn = null;
        try {
          conn = (HttpURLConnection) new URL(baseUrl).openConnection();
          conn.setRequestProperty("Content-Length", Long.toString(batchSize));
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestMethod("PUT");
          conn.setUseCaches(false);
          conn.setDoInput(true);
          conn.setDoOutput(true);

          OutputStream os = null;
          try {
            os = conn.getOutputStream();

            byte[] bytes = new byte[2048];
            FileInputStream fis = null;
            try {
              fis = new FileInputStream(batchFile);
              int length = -1;
              while ((length = fis.read(bytes)) > -1) {
                os.write(bytes, 0, length);
              }
            } finally {
              if (fis != null) fis.close();
            }

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
