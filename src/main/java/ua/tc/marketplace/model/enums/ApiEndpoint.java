package ua.tc.marketplace.model.enums;

import org.springframework.http.HttpMethod;

public enum ApiEndpoint {
    GET_USER("/api/users/{id}", HttpMethod.GET, "USER"),
    CREATE_USER("/api/users", HttpMethod.POST, "ADMIN"),
    UPDATE_USER("/api/users/{id}", HttpMethod.PUT, "ADMIN"),
    DELETE_USER("/api/users/{id}", HttpMethod.DELETE, "ADMIN"),
    PUBLIC_HOME("/public", HttpMethod.GET, "ANONYMOUS");

    private final String path;
    private final HttpMethod method;
    private final String requiredRole;

    ApiEndpoint(String path, HttpMethod method, String requiredRole) {
        this.path = path;
        this.method = method;
        this.requiredRole = requiredRole;
    }








}
