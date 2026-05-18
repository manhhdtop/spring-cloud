package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("unused")
public class UserNotFoundException extends RuntimeException {
    private static final UserNotFoundException INSTANCE = new UserNotFoundException();

    private UserNotFoundException() {
        super(MessageKeys.USER_NOT_FOUND);
    }

    public static UserNotFoundException getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(MessageKeys.USER_NOT_FOUND);
    }
}
