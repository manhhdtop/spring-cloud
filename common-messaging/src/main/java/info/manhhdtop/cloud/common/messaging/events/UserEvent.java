package info.manhhdtop.cloud.common.messaging.events;

import info.manhhdtop.cloud.common.core.dtos.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserEvent extends UserDto {
    private EventType eventType;
    private OffsetDateTime timestamp;
    private String reason;

    public enum EventType {
        USER_SYNC,
        USER_CREATED,
        USER_UPDATED,
        USER_LOCKED,
        USER_UNLOCKED,
        USER_DELETED,
        USER_STATUS_CHANGED
    }
}

