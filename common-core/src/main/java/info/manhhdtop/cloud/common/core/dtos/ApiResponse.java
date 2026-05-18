package info.manhhdtop.cloud.common.core.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import info.manhhdtop.cloud.common.core.constants.ApiResponseCode;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.AbstractMessageUtil;
import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@SuppressWarnings("unused")
public class ApiResponse<T> {
    private static final String MESSAGE_BAD_REQUEST = MessageKeys.BAD_REQUEST;

    private boolean success;
    private int status;
    @Builder.Default
    private ApiResponseCode code = ApiResponseCode.SUCCESS;
    private String error;
    private String message;
    private T data;
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return build(true, message, data);
    }

    public static <T> ApiResponse<T> error(int status) {
        return build(false, status, null, MESSAGE_BAD_REQUEST, MessageKeys.SERVER_ERROR, null);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return build(false, status, null, MESSAGE_BAD_REQUEST, message, null);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return build(false, status, null, error, message, null);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message, T data) {
        return build(false, status, null, error, message, data);
    }

    public static <T> ApiResponse<T> error(int status, ApiResponseCode code, String error, String message) {
        return build(false, status, code, error, message, null);
    }

    public static <T> ApiResponse<T> error(int status, ApiResponseCode code, String error, String message, T data) {
        return build(false, status, code, error, message, data);
    }

    public static <T> ApiResponse<T> error(
            int status, ApiResponseCode code, String error, String messageKey, Object[] args) {
        return build(false, status, code, error, messageKey, args, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(MessageKeys.SUCCESS, data);
    }

    public static <T> ApiResponse<T> build(boolean success, String message, T data) {
        return build(success, 200, ApiResponseCode.SUCCESS, null, message, data);
    }

    public static <T> ApiResponse<T> build(boolean success, String error, String message, T data) {
        return build(success, 200, ApiResponseCode.SUCCESS, error, message, data);
    }

    public static <T> ApiResponse<T> build(boolean success, int status, String error, String message, T data) {
        ApiResponseCode code = success ? ApiResponseCode.SUCCESS : null;
        return build(success, status, code, error, message, data);
    }

    public static <T> ApiResponse<T> build(
            boolean success, int status, ApiResponseCode code, String error, String message, T data) {
        return build(success, status, code, error, message, null, data);
    }

    public static <T> ApiResponse<T> build(
            boolean success,
            int status,
            ApiResponseCode code,
            String error,
            String messageKey,
            Object[] args,
            T data) {
        return ApiResponse.<T>builder()
                .success(success)
                .status(status)
                .code(code != null ? code : (success ? ApiResponseCode.SUCCESS : null))
                .error(error)
                .message(AbstractMessageUtil.get(messageKey, args))
                .data(data)
                .build();
    }
}
