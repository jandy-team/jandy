package io.jandy.test;

import io.undertow.Undertow;
import io.undertow.io.Receiver;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.nio.charset.Charset;

/**
 * @author JCooky
 * @since 2016-06-07
 */
public class TestWebServer {
  private static final Charset UTF_8 = Charset.forName("UTF-8");

  private static Receiver.FullStringCallback sysout = new Receiver.FullStringCallback() {
    @Override
    public void handle(HttpServerExchange exchange, String message) {
      System.out.println("Receive ["+exchange.getRequestMethod()+"] --> " + message);
//      System.out.println(message);
    }
  };

  public static void main(String[] args) {
    Undertow server = Undertow.builder()
        .addHttpListener(3000, "localhost")
        .setHandler(new HttpHandler() {
          @Override
          public void handleRequest(HttpServerExchange exchange) throws Exception {
            if (exchange.getRequestPath().equalsIgnoreCase("/rest/travis")) {
//              System.out.println("Headers: " + exchange.getRequestHeaders());
              exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");
              if (exchange.getRequestMethod().equalToString("POST")) {
                exchange.getRequestReceiver().receiveFullString(sysout, UTF_8);
                exchange.getResponseSender().send("{\"profId\": 1}");
              } else if (exchange.getRequestMethod().equalToString("PUT")) {
                exchange.getRequestReceiver().receiveFullString(sysout, UTF_8);
              } else if (exchange.getRequestMethod().equalToString("DELETE")) {
                exchange.getRequestReceiver().receiveFullString(sysout, UTF_8);
              }
            } else {
              System.out.println(exchange.getRequestPath() + " is can't");
            }
          }
        }).build();
    server.start();
  }
}
