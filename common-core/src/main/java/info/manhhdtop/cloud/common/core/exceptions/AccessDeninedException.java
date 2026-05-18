package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@SuppressWarnings("unused")
public class AccessDeninedException extends RuntimeException {
    private static final AccessDeninedException INSTANCE = new AccessDeninedException();

    private AccessDeninedException() {
        super(MessageKeys.ACCESS_DENIED);
    }

    public static AccessDeninedException getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(MessageKeys.ACCESS_DENIED);
    }
}
