package info.manhhdtop.cloud.common.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
@SuppressWarnings("unused")
public class AccessDeninedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Access denied";

    private static final AccessDeninedException INSTANCE = new AccessDeninedException();

    private AccessDeninedException() {
        super(DEFAULT_MESSAGE);
    }

    public static AccessDeninedException getInstance() {
        return INSTANCE;
    }
}
