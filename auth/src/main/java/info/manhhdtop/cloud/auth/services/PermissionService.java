package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.dtos.requests.CreatePermissionRequest;

import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import java.util.List;

public interface PermissionService {
    PermissionDto create(CreatePermissionRequest request);

    PermissionDto getById(Long id);

    List<PermissionDto> getAll();

    PermissionDto update(Long id, CreatePermissionRequest request);

    void delete(Long id);
}

