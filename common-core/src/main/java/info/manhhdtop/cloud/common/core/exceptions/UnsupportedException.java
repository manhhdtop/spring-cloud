package info.manhhdtop.cloud.common.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
@SuppressWarnings("unused")
public class UnsupportedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Unsupported Operation";

    private static final UnsupportedException INSTANCE = new UnsupportedException();

    private UnsupportedException() {
        super(DEFAULT_MESSAGE);
    }

    public static UnsupportedException getInstance() {
        return INSTANCE;
    }
}
