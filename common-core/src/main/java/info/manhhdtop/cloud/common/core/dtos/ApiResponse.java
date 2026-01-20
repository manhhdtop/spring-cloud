package info.manhhdtop.cloud.common.core.dtos;

import info.manhhdtop.cloud.common.core.utils.AbstractMessageUtil;
import lombok.*;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@SuppressWarnings("unused")
public class ApiResponse<T> {
    private static final String MESSAGE_SUCCESS = "Success";
    private static final String MESSAGE_ERROR = "Server error";
    private static final String MESSAGE_BAD_REQUEST = "Bad request";

    private boolean success;
    private int status;
    private String error;
    private String message;
    private T data;
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private OffsetDateTime timestamp = OffsetDateTime.now();

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return success(MESSAGE_SUCCESS, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return build(true, message, data);
    }

    public static <T> ApiResponse<T> error(int status) {
        return build(false, status, MESSAGE_BAD_REQUEST, MESSAGE_ERROR, null);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return build(false, status, MESSAGE_BAD_REQUEST, message, null);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message) {
        return build(false, status, error, message, null);
    }

    public static <T> ApiResponse<T> error(int status, String error, String message, T data) {
        return build(false, status, error, message, data);
    }

    public static <T> ApiResponse<T> build(boolean success, String message, T data) {
        return build(success, 200, null, message, data);
    }

    public static <T> ApiResponse<T> build(boolean success, String error, String message, T data) {
        return build(success, 200, null, message, data);
    }

    public static <T> ApiResponse<T> build(boolean success, int status, String error, String message, T data) {
        return ApiResponse.<T>builder()
                .success(success)
                .status(status)
                .error(error)
                .message(AbstractMessageUtil.get(message))
                .data(data)
                .build();
    }
}
