package io.jandy.test;

import io.undertow.Undertow;
import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class TestWebServer {
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  private static final boolean logging = true;
  private static final File loggingFile = new File("/tmp/a.log");

  private static Receiver.FullStringCallback sysout = new Receiver.FullStringCallback() {
    @Override
    public void handle(HttpServerExchange exchange, String message) {
      String str = "Receive '"+exchange.getRequestPath()+"' ["+exchange.getRequestMethod()+"] --> " + message.replace("\n", "\\n") + "\n";
      System.out.println(str);
      if (logging) {
        try {
          FileUtils.writeStringToFile(loggingFile, str, UTF_8, true);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  };

  public static void main(String[] args) {
    loggingFile.delete();
    Undertow server = Undertow.builder()
        .addHttpListener(3000, "localhost")
        .setHandler(new HttpHandler() {
          @Override
          public void handleRequest(HttpServerExchange exchange) throws Exception {
            exchange.getRequestReceiver().receiveFullString(sysout, UTF_8);
            if (exchange.getRequestPath().startsWith("/rest/travis")) {
              exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
              if (exchange.getRequestPath().equals("/rest/travis/prof/create") && exchange.getRequestMethod().equalToString("POST")) {
                exchange.getResponseSender().send("{\"profId\": 1}");
              }
            }
            exchange.endExchange();
          }
        }).build();
    server.start();
  }
}
