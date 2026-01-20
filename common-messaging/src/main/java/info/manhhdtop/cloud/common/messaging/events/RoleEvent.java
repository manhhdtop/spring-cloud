package info.manhhdtop.cloud.common.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RoleEvent implements Serializable {
    private EventType eventType;
    private String roleName;
    private String description;
    private Set<Long> permissionIds;
    private OffsetDateTime timestamp;

    public enum EventType {
        ROLE_CREATE_REQUEST,
        ROLE_CREATED,
        ROLE_UPDATED,
        ROLE_DELETED
    }
}

