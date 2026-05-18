package info.manhhdtop.cloud.common.core.constants;

public interface ServiceConstant {
    String APP_PREFIX = "/api/v1/";

    interface Auth {
        String MODULE_NAME = "AUTH";
        String URL_LOAD_BALANCE = "lb://auth";
        String BASE_URL = APP_PREFIX + "auth/";
        String LOGIN = "login";
        String REGISTER = "register";
        String LOGOUT = "logout";
        String REFRESH_TOKEN = "refresh-token";
        String FORGOT_PASSWORD = "forgot-password";
        String CHANGE_PASSWORD = "change-password";
        String VALIDATE = "validate";
    }

    interface User {
        String MODULE_NAME = "USER";
        String URL_LOAD_BALANCE = "lb://users";
        String BASE_URL = APP_PREFIX + "users/";
        String GET_ME = "me";
        String GET_USER_BY_ID = "{id}";
    }
}
