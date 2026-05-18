package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@SuppressWarnings("unused")
public class UnauthorizedException extends RuntimeException {
    private static final UnauthorizedException INSTANCE = new UnauthorizedException();

    private UnauthorizedException() {
        super(MessageKeys.UNAUTHORIZED);
    }

    public static UnauthorizedException getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(MessageKeys.UNAUTHORIZED);
    }
}
