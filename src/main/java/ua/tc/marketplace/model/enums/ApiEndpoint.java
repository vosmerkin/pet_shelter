package ua.tc.marketplace.model.enums;

import static java.util.Collections.EMPTY_SET;
import static org.springframework.http.HttpMethod.GET;
import static ua.tc.marketplace.config.ApiURLs.*;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.Set;

@Getter
public enum ApiEndpoint {

  // Ad
  AD_GET_ALL_ENDPOINT(AD_BASE + AD_GET_ALL, GET, false),
  AD_GET_ALL_COUNTED_ENDPOINT(AD_BASE+AD_GET_ALL_COUNTED, GET, false),
  AD_GET_BY_ID_ENDPOINT(AD_BASE+AD_GET_BY_ID, GET, false),
  AD_CREATE_ENDPOINT(AD_BASE+AD_CREATE,HttpMethod.POST, true),
  AD_UPDATE_ENDPOINT(AD_BASE+AD_UPDATE,HttpMethod.PUT, true),
  AD_DELETE_ENDPOINT(AD_BASE+AD_DELETE,HttpMethod.DELETE, true),

  //User
  USER_GET_ALL_ENDPOINT(USER_BASE +USER_GET_ALL, GET, true),
  USER_GET_BY_ID_ENDPOINT(USER_BASE +USER_GET_BY_ID, GET,false),
  USER_FULL_GET_BY_ID_ENDPOINT(USER_BASE +USER_FULL_GET_BY_ID, GET,false),
  USER_GET_BY_EMAIL_ENDPOINT(USER_BASE +USER_GET_BY_EMAIL, GET,false),
  USER_UPDATE_ENDPOINT(USER_BASE +USER_UPDATE,HttpMethod.PUT, true),
  USER_DELETE_ENDPOINT(USER_BASE +USER_DELETE,HttpMethod.DELETE, true);

  private final String path;
  private final HttpMethod method;
  private final boolean secured;
//  private final Set<String> requiredAuthorities;





  ApiEndpoint(String path, HttpMethod method, boolean secured) {
//  ApiEndpoint(String path, HttpMethod method, boolean secured, Set<String> requiredAuthorities) {
    this.path = path;
    this.method = method;
    this.secured = secured;
//    this.requiredAuthorities = Set.copyOf(requiredAuthorities); // immutable copy
  }

}
