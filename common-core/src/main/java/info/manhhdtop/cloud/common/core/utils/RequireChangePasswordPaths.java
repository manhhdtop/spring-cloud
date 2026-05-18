package info.manhhdtop.cloud.common.core.utils;

import info.manhhdtop.cloud.common.core.constants.ServiceConstant;
import java.util.Set;

public final class RequireChangePasswordPaths {

    private static final String AUTH_BASE = ServiceConstant.Auth.BASE_URL;

    private static final Set<String> ALLOWED_EXACT_PATHS = Set.of(
            AUTH_BASE + ServiceConstant.Auth.LOGIN,
            AUTH_BASE + ServiceConstant.Auth.REGISTER,
            AUTH_BASE + ServiceConstant.Auth.LOGOUT,
            AUTH_BASE + ServiceConstant.Auth.CHANGE_PASSWORD,
            AUTH_BASE + ServiceConstant.Auth.FORGOT_PASSWORD,
            AUTH_BASE + ServiceConstant.Auth.REFRESH_TOKEN
    );

    private RequireChangePasswordPaths() {
    }

    public static boolean isAllowed(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        String normalized = normalizePath(path);
        if (isSwaggerPath(normalized)) {
            return true;
        }
        return ALLOWED_EXACT_PATHS.contains(normalized);
    }

    private static String normalizePath(String path) {
        String normalized = path.strip();
        int queryIndex = normalized.indexOf('?');
        if (queryIndex >= 0) {
            normalized = normalized.substring(0, queryIndex);
        }
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static boolean isSwaggerPath(String path) {
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.equals("/swagger-ui.html")
                || path.contains("/swagger")
                || path.contains("/api-docs");
    }
}
