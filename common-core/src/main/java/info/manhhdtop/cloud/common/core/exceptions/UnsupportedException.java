package info.manhhdtop.cloud.common.core.exceptions;

import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@SuppressWarnings("unused")
public class UnsupportedException extends RuntimeException {
    private static final UnsupportedException INSTANCE = new UnsupportedException();

    private UnsupportedException() {
        super(MessageKeys.UNSUPPORTED_OPERATION);
    }

    public static UnsupportedException getInstance() {
        return INSTANCE;
    }

    @Override
    public String getMessage() {
        return MessageUtil.get(MessageKeys.UNSUPPORTED_OPERATION);
    }
}
