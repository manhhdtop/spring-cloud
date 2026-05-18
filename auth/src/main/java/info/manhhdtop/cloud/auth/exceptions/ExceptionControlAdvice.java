package info.manhhdtop.cloud.auth.exceptions;

import info.manhhdtop.cloud.common.core.constants.ApiResponseCode;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.exceptions.AccessDeninedException;
import info.manhhdtop.cloud.common.core.exceptions.AccessTokenExpiredException;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import info.manhhdtop.cloud.common.core.exceptions.RequireChangePasswordException;
import info.manhhdtop.cloud.common.core.exceptions.UnauthorizedException;
import info.manhhdtop.cloud.common.core.utils.ApiResponseFactory;
import info.manhhdtop.cloud.common.core.utils.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ExceptionControlAdvice {
    private static @NonNull ResponseEntity<ApiResponse<Object>> getResponse(HttpStatus status,
            ApiResponseCode code,
            String ex) {
        var error = ApiResponse.error(
                status.value(),
                code,
                status.getReasonPhrase(),
                ex
        );

        return ResponseEntity.status(status.value()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest ignored) {
        var errors = ValidationErrors.from(ex.getBindingResult());

        var error = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                MessageKeys.ARGUMENTS_INVALID,
                errors
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleBusiness(ApplicationException ex) {
        return getResponse(HttpStatus.BAD_REQUEST, ApiResponseCode.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<?> tokenExpired(AccessTokenExpiredException ex) {
        return getResponse(HttpStatus.UNAUTHORIZED, ApiResponseCode.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedException(UnauthorizedException ex) {
        var status = HttpStatus.UNAUTHORIZED;
        return getResponse(status, ApiResponseCode.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeninedException.class)
    public ResponseEntity<?> accessDenied(AccessDeninedException ex) {
        return getResponse(HttpStatus.FORBIDDEN, ApiResponseCode.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(RequireChangePasswordException.class)
    public ResponseEntity<?> requireChangePassword(RequireChangePasswordException ignored) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseFactory.requireChangePassword());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleStatus(ResponseStatusException ex, HttpServletRequest ignored) {
        HttpStatusCode status = ex.getStatusCode();

        var error = ApiResponse.error(
                status.value(),
                ex.getDetailMessageCode(),
                ex.getReason()
        );

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex, HttpServletRequest ignored) {
        return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, ApiResponseCode.SERVER_ERROR, ex.getMessage());
    }
}
