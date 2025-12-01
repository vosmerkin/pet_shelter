package ua.tc.marketplace.config;

public class ApiURLs {
    //Swagger
    public static final String SWAGGER_UI_PAGES = "/swagger-ui/**";
    public static final String SWAGGER_UI_MAIN_PAGE = "/swagger-ui.html";
    public static final String SWAGGER_DOCS = "/v3/api-docs/**";

    //Ad controller
    public static final String AD_BASE = "/api/v1/ad";
    public static final String AD_GET_ALL = "";
    public static final String AD_GET_ALL_COUNTED = "/counted";
    public static final String AD_GET_BY_ID = "/{adId}";
    public static final String AD_CREATE = "";
    public static final String AD_UPDATE = "/{adId}";
    public static final String AD_DELETE = "/{adId}";

    //Article controller
    public static final String ARTICLE_BASE = "/api/v1/article";
    public static final String ARTICLE_GET_ALL = "";
    public static final String ARTICLE_GET_BY_ID = "/{id}";
    public static final String ARTICLE_CREATE = "";
    public static final String ARTICLE_UPDATE = "/{id}";
    public static final String ARTICLE_DELETE = "/{id}";

    //Attribute controller
    public static final String ATTRIBUTE_BASE = "/api/v1/attribute";
    public static final String ATTRIBUTE_GET_ALL = "";
    public static final String ATTRIBUTE_GET_BY_ID = "/{id}";
    public static final String ATTRIBUTE_CREATE = "";
    public static final String ATTRIBUTE_UPDATE = "/{id}";
    public static final String ATTRIBUTE_DELETE = "/{id}";

    //AuthController
    public static final String AUTH_BASE = "/api/v1/auth";
    public static final String AUTH_LOGIN = "/login";
    public static final String AUTH_SIGNUP = "/signup";
    public static final String AUTH_VERIFY_EMAIL = "/verify-email";
    public static final String AUTH_VERIFY_EMAIL_LOGIN = "/verify-email-login";
    public static final String AUTH_VERIFY_EMAIL_WITH_TOKEN = "/verify-email?token=";
    public static final String AUTH_FORGET_PASSWORD = "/forget_password";
    public static final String AUTH_VERIFY_PASSWORD_RESET = "/verify-reset";
    public static final String AUTH_VERIFY_PASSWORD_RESET_WITH_TOKEN = "/verify-reset?token=";
    public static final String AUTH_RESET_PASSWORD = "/reset_password";
    public static final String AUTH_SIGNUP_WITH_VERIFY = "/signup_verify";
    public static final String LIST_TOKENS = "/list_tokens";

    //CategoryController
    public static final String CATEGORY_BASE = "/api/v1/category";
    public static final String CATEGORY_GET_ALL = "";
    public static final String CATEGORY_GET_ALL_COUNTED = "/counted";
    public static final String CATEGORY_BY_ID = "/{id}";
    public static final String CATEGORY_ATTRIBUTE_BY_IDS = "/{categoryId}/attribute/{attributeId}";
    public static final String CATEGORY_ATTRIBUTES_BY_CATEGORY_ID = "/{categoryId}/attribute";
    public static final String CATEGORY_CREATE = "";
    public static final String CATEGORY_UPDATE = "/{id}";
    public static final String CATEGORY_ATTRIBUTE_UPDATE = "/{categoryId}/attribute/{attributeId}";
    public static final String CATEGORY_DELETE = "/{id}";

    //Comment controller
    public static final String COMMENT_BASE = "/api/v1/comment";
    public static final String COMMENT_GET_ALL = "/all";
    public static final String COMMENT_GET_BY_ID = "/{id}";
    public static final String COMMENT_CREATE = "";
    public static final String COMMENT_UPDATE = "/{id}";
    public static final String COMMENT_DELETE = "/{id}";

    //Photo controller
    public static final String PHOTO_BASE = "/api/v1/photo";
    public static final String PHOTO_SAVE_AD_PHOTO_FILES = "/ad/{adId}";
    public static final String PHOTO_DELETE_AD_PHOTOS_WITH_FILES = "/ad/{adId}";
    public static final String PHOTO_FIND_ALL_BY_ADID = "/ad/{adId}";
    public static final String PHOTO_SAVE_USER_PHOTO_FILES = "/user/{userId}";
    public static final String PHOTO_DELETE_USER_PROFILE_PICTURE = "/user/{userId}";
    public static final String PHOTO_BY_USERID = "/user/{userId}";

    //Photo files controller
    public static final String PHOTO_FILES_BASE = "/api/v1/file";
    public static final String PHOTO_FILES_FIND_ALL_BY_ADID = "/ad/{adId}";
    public static final String PHOTO_FILES_FIND_AD_PHOTO_FILE_BY_IDS = "/ad/{adId}/photo/{photoId}";
    public static final String PHOTO_FILES_FIND_PROFILE_PICTURE_FILE_BY_USERID = "/user/{userId}";

    //SampleDataController
    public static final String SAMPLE_DATA_BASE = "/api/v1/sample_data";
    public static final String SAMPLE_DATA_ADD_ADS = "/add_ads";

    //Tag controller
    public static final String TAG_BASE = "/api/v1/tag";
    public static final String TAG_GET_ALL = "/all";
    public static final String TAG_GET_BY_ID = "/{id}";
    public static final String TAG_CREATE = "";
    public static final String TAG_UPDATE = "/{id}";
    public static final String TAG_DELETE = "/{id}";

    //UserController
    public static final String USER_BASE = "/api/v1/user";
    public static final String USER_GET_ALL = "/all";
    public static final String USER_GET_BY_ID = "/{id}";
    public static final String USER_FULL_GET_BY_ID = "/{id}/full";
    public static final String USER_GET_BY_EMAIL = "/email/{email}";
    public static final String USER_UPDATE = "";
    public static final String USER_DELETE = "/{id}";



}
