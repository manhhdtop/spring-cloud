package info.manhhdtop.cloud.auth.services.impl;

import info.manhhdtop.cloud.auth.dtos.requests.CreatePermissionRequest;
import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.auth.repositories.PermissionRepository;
import info.manhhdtop.cloud.auth.services.PermissionService;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public PermissionDto create(CreatePermissionRequest request) {
        // Check if permission name already exists
        if (permissionRepository.findByName(request.name()).isPresent()) {
            throw new ApplicationException(MessageKeys.PERMISSION_NAME_EXISTS, request.name());
        }

        Permission permission = new Permission();
        permission.setName(request.name());
        permission.setModule(request.module());
        permission.setDescription(request.description());

        Permission saved = permissionRepository.save(permission);
        return mapToDto(saved);
    }

    @Override
    public PermissionDto getById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(MessageKeys.PERMISSION_NOT_FOUND_BY_ID, id));
        return mapToDto(permission);
    }

    @Override
    public List<PermissionDto> getAll() {
        return permissionRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PermissionDto update(Long id, CreatePermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(MessageKeys.PERMISSION_NOT_FOUND_BY_ID, id));

        // Check if name is being changed and if new name already exists
        if (!permission.getName().equals(request.name())) {
            if (permissionRepository.findByName(request.name()).isPresent()) {
                throw new ApplicationException(MessageKeys.PERMISSION_NAME_EXISTS, request.name());
            }
        }

        permission.setName(request.name());
        permission.setModule(request.module());
        permission.setDescription(request.description());

        Permission updated = permissionRepository.save(permission);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(MessageKeys.PERMISSION_NOT_FOUND_BY_ID, id));
        permissionRepository.delete(permission);
    }

    private PermissionDto mapToDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .module(permission.getModule())
                .description(permission.getDescription())
                .status(permission.getStatus())
                .build();
    }
}

