package info.manhhdtop.cloud.common.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@SuppressWarnings("unused")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApplicationException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Unknown error";

    private String message;

    public ApplicationException() {
        this.message = ERROR_MESSAGE;
    }
}
