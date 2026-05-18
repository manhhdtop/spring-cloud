package info.manhhdtop.cloud.common.core.utils;

import info.manhhdtop.cloud.common.core.constants.ApiResponseCode;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.exceptions.RequireChangePasswordException;
import org.springframework.http.HttpStatus;

public final class ApiResponseFactory {

    private ApiResponseFactory() {
    }

    public static ApiResponse<Object> requireChangePassword() {
        return ApiResponse.error(
                HttpStatus.FORBIDDEN.value(),
                ApiResponseCode.REQUIRE_CHANGE_PASSWORD,
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                RequireChangePasswordException.MESSAGE
        );
    }

    public static ApiResponse<Object> serviceUnavailable(String service) {
        String message = service != null && !service.isBlank()
                ? MessageKeys.SERVICE_UNAVAILABLE_NAMED
                : MessageKeys.SERVICE_UNAVAILABLE;
        Object[] args = service != null && !service.isBlank() ? new Object[] {service.strip()} : null;
        return ApiResponse.error(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ApiResponseCode.SERVICE_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                message,
                args
        );
    }
}
