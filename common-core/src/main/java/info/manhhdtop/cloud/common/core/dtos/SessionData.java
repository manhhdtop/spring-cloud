package info.manhhdtop.cloud.common.core.dtos;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SessionData {

    private UserDto user;
    private String deviceId;
    private String userAgent;
    private String ip;
    private List<RoleDto> roles;
    private Set<PermissionDto> permissions;
}

