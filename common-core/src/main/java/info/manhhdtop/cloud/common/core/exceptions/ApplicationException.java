package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.ApiResponseCode;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
@SuppressWarnings("unused")
public class ApplicationException extends RuntimeException {
    private static ApplicationException INSTANCE;

    private final ApiResponseCode code;
    private final String messageKey;
    private final transient Object[] args;

    public ApplicationException() {
        this(MessageKeys.BAD_REQUEST);
    }

    public ApplicationException(String messageKey, Object... args) {
        this.code = ApiResponseCode.BAD_REQUEST;
        this.messageKey = messageKey;
        this.args = args;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(messageKey, args);
    }

    public static Exception getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationException(MessageKeys.UNKNOWN_ERROR);
        }
        return INSTANCE;
    }
}
