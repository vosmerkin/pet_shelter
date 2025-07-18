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
    public static final String AUTH_VERIFY_EMAIL = "/verify-email";
    public static final String AUTH_VERIFY_EMAIL_WITH_TOKEN = "/verify-email?token=";
    public static final String AUTH_FORGET_PASSWORD = "/forget_password";
    public static final String AUTH_VERIFY_PASSWORD_RESET = "/verify-reset";
    public static final String AUTH_VERIFY_PASSWORD_RESET_WITH_TOKEN = "/verify-reset?token=";
    public static final String AUTH_RESET_PASSWORD = "/reset_password";

    //UserController
    public static final String USER_BASE = "/api/v1/user";
    public static final String USER_GET_ALL = "/all";
    public static final String USER_GET_BY_ID = "/{id}";
    public static final String USER_GET_BY_EMAIL = "/email/{email}";
    public static final String USER_UPDATE = "";
    public static final String USER_DELETE = "/{id}";


    //SampleDataController
    public static final String SAMPLE_DATA_BASE = "/api/v1/sample_data";
    public static final String SAMPLE_DATA_ADD_ADS = "/add_ads";
}
