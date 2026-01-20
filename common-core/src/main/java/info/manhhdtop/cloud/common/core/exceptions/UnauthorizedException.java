package info.manhhdtop.cloud.common.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
@SuppressWarnings("unused")
public class UnauthorizedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unauthorized";

    private static final UnauthorizedException INSTANCE = new UnauthorizedException();

    private UnauthorizedException() {
        super(DEFAULT_MESSAGE);
    }

    public static UnauthorizedException getInstance() {
        return INSTANCE;
    }
}
