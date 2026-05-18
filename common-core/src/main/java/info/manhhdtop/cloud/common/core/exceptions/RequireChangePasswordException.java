package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
@SuppressWarnings("unused")
public class RequireChangePasswordException extends RuntimeException {
    public static final String MESSAGE = MessageKeys.REQUIRE_CHANGE_PASSWORD;

    private static final RequireChangePasswordException INSTANCE = new RequireChangePasswordException();

    private RequireChangePasswordException() {
        super(MESSAGE);
    }

    public static RequireChangePasswordException getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(MessageKeys.REQUIRE_CHANGE_PASSWORD);
    }
}
