package info.manhhdtop.cloud.common.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
@SuppressWarnings("unused")
public class AccessTokenExpiredException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Token has expired";

    private static final AccessTokenExpiredException INSTANCE = new AccessTokenExpiredException();

    private AccessTokenExpiredException() {
        super(DEFAULT_MESSAGE);
    }

    public static AccessTokenExpiredException getInstance() {
        return INSTANCE;
    }
}
