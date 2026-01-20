package info.manhhdtop.cloud.common.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PermissionEvent implements Serializable {
    private EventType eventType;
    private String permissionName;
    private String module;
    private String description;
    private OffsetDateTime timestamp;

    public enum EventType {
        PERMISSION_CREATE_REQUEST,
        PERMISSION_CREATED,
        PERMISSION_UPDATED,
        PERMISSION_DELETED
    }
}

