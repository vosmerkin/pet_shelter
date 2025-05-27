package ua.tc.marketplace.config;

public class ApiURLs {
    //Ad controller
    public static final String AD_BASE = "/api/v1/ad";
    public static final String AD_GET_ALL = "";
    public static final String AD_GET_ALL_COUNTED = "/counted";
    public static final String AD_GET_BY_ID = "/{adId}";
    public static final String AD_CREATE = "";
    public static final String AD_UPDATE = "/{adId}";
    public static final String AD_DELETE = "/{adId}";

    //AuthController
    public static final String AUTH_BASE = "/api/v1/auth";
    public static final String AUTH_VERIFY_EMAIL = "/verify-email?token=";


    //UserController

    public static final String USER_BASE = "/api/v1/user";
    public static final String USER_GET_ALL = "/all";
    public static final String USER_GET_BY_ID = "/{id}";
    public static final String USER_GET_BY_EMAIL = "/email/{email}";
    public static final String USER_UPDATE = "";
    public static final String USER_DELETE = "/{id}";
}
