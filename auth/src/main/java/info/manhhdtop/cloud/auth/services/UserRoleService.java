package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.dtos.requests.AssignRoleToUserRequest;

import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import java.util.List;

public interface UserRoleService {
    void assignRolesToUser(AssignRoleToUserRequest request);

    void removeRolesFromUser(Long userId, List<Long> roleIds);

    List<RoleDto> getUserRoles(Long userId);

    List<RoleDto> getAvailableRoles(Long userId);
}

