package info.manhhdtop.cloud.users.exceptions;

import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest ignored) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .toList();

        var error = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Arguments invalid",
                errors
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleBusiness(ApplicationException ex, HttpServletRequest req) {
        var error = ApiResponse.error(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.badRequest().body(error);
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
        var error = ApiResponse.error(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.internalServerError().body(error);
    }
}
