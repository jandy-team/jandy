package io.jandy.util.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author JCooky
 * @since 2016-04-10
 */
public class ClientHttpResponseWrapper implements ClientHttpResponse {
  private ClientHttpResponse delegate;

  public ClientHttpResponseWrapper(ClientHttpResponse delegate) {
    this.delegate = delegate;
  }

  @Override
  public HttpStatus getStatusCode() throws IOException {
    return delegate.getStatusCode();
  }

  @Override
  public int getRawStatusCode() throws IOException {
    return delegate.getRawStatusCode();
  }

  @Override
  public String getStatusText() throws IOException {
    return delegate.getStatusText();
  }

  @Override
  public void close() {
    delegate.close();
  }

  @Override
  public InputStream getBody() throws IOException {
    return delegate.getBody();
  }

  @Override
  public HttpHeaders getHeaders() {
    return delegate.getHeaders();
  }
}
