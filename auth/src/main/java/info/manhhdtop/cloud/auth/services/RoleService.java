package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.dtos.requests.CreateRoleRequest;

import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import java.util.List;

public interface RoleService {
    RoleDto create(CreateRoleRequest request);

    RoleDto getById(Long id);

    List<RoleDto> getAll();

    RoleDto update(Long id, CreateRoleRequest request);

    void delete(Long id);

    RoleDto assignPermissions(Long roleId, List<Long> permissionIds);
}

