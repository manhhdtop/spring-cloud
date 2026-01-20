package info.manhhdtop.cloud.common.core.constants;

public interface HeaderConstant {
    String AUTHORIZATION = "Authorization";
    String USER_AGENT = "User-Agent";
    String TOKEN_PREFIX = "Bearer ";
    String X_USER_ID = "X-User-Id";
    /** Comma-separated permission names (gateway or internal calls). */
    String X_SESSION_ID = "X-Session-Id";
    String X_REQUEST_ID = "X-Request-Id";
}
