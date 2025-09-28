package ua.tc.marketplace.model.enums;

import org.springframework.http.HttpMethod;
import ua.tc.marketplace.config.ApiURLs;

import static ua.tc.marketplace.config.ApiURLs.*;

public enum ApiEndpoint {

  //Ad

  GET_ALL_ADS(AD_BASE + AD_GET_ALL,HttpMethod.GET ),
  GET_ALL_ADS_COUNTED(AD_BASE+AD_GET_ALL_COUNTED,HttpMethod.GET),


  GET_USER("/api/users/{id}", HttpMethod.GET),
  CREATE_USER("/api/users", HttpMethod.POST),
  UPDATE_USER("/api/users/{id}", HttpMethod.PUT),
  DELETE_USER("/api/users/{id}", HttpMethod.DELETE),
  PUBLIC_HOME("/public", HttpMethod.GET);

  private final String path;
  private final HttpMethod method;

  ApiEndpoint(String path, HttpMethod method) {
    this.path = path;
    this.method = method;
  }

  public String getPath() {
    return path;
  }

  public HttpMethod getMethod() {
    return method;
  }
}
