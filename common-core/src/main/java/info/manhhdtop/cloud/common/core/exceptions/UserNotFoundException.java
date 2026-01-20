package info.manhhdtop.cloud.common.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("unused")
public class UserNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User not found";
    private static final UserNotFoundException INSTANCE = new UserNotFoundException();

    private Long userId;

    private UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    private UserNotFoundException(Long userId) {
        super(DEFAULT_MESSAGE);
        this.userId = userId;
    }

    public static UserNotFoundException getInstance() {
        return INSTANCE;
    }

    public static UserNotFoundException of(Long userId) {
        return new UserNotFoundException(userId);
    }
}
