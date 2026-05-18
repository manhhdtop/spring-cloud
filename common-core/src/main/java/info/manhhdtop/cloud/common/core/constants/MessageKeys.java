package info.manhhdtop.cloud.common.core.constants;

public final class MessageKeys {
    private MessageKeys() {
    }

    public static final String SUCCESS = "common.success";
    public static final String SERVER_ERROR = "common.server_error";
    public static final String BAD_REQUEST = "common.bad_request";
    public static final String UNKNOWN_ERROR = "common.unknown_error";
    public static final String ARGUMENTS_INVALID = "common.arguments_invalid";

    public static final String UNAUTHORIZED = "error.unauthorized";
    public static final String ACCESS_DENIED = "error.access_denied";
    public static final String TOKEN_EXPIRED = "error.token_expired";
    public static final String REQUIRE_CHANGE_PASSWORD = "error.require_change_password";
    public static final String USER_NOT_FOUND = "error.user_not_found";
    public static final String UNSUPPORTED_OPERATION = "error.unsupported_operation";

    public static final String USER_EMAIL_EXISTS = "error.user.email_exists";
    public static final String USER_NOT_FOUND_BY_ID = "error.user.not_found_by_id";
    public static final String PASSWORD_INCORRECT = "error.password.incorrect";

    public static final String PERMISSION_NAME_EXISTS = "error.permission.name_exists";
    public static final String PERMISSION_NOT_FOUND_BY_ID = "error.permission.not_found_by_id";

    public static final String ROLE_NAME_EXISTS = "error.role.name_exists";
    public static final String ROLE_NOT_FOUND_BY_ID = "error.role.not_found_by_id";
    public static final String PERMISSIONS_NOT_FOUND = "error.permissions.not_found";
    public static final String ROLES_NOT_FOUND = "error.roles.not_found";

    public static final String SERVICE_UNAVAILABLE = "error.service.unavailable";
    public static final String SERVICE_UNAVAILABLE_NAMED = "error.service.unavailable_named";

    public static final String VALIDATION_NOT_SAME_VALUE = "validation.not_same_value";
}
