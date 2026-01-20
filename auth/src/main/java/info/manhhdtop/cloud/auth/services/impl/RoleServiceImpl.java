package info.manhhdtop.cloud.auth.services.impl;

import info.manhhdtop.cloud.auth.dtos.requests.CreateRoleRequest;
import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.auth.repositories.PermissionRepository;
import info.manhhdtop.cloud.auth.repositories.RoleRepository;
import info.manhhdtop.cloud.auth.services.RoleService;
import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public RoleDto create(CreateRoleRequest request) {
        // Check if role name already exists
        if (roleRepository.findByName(request.name()).isPresent()) {
            throw new ApplicationException("Role with name '" + request.name() + "' already exists");
        }

        Role role = new Role();
        role.setName(request.name());
        role.setDescription(request.description());

        // Assign permissions if provided
        if (request.permissionIds() != null && !request.permissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            if (permissions.size() != request.permissionIds().size()) {
                throw new ApplicationException("Some permissions not found");
            }
            role.setPermissions(permissions);
        }

        Role saved = roleRepository.save(role);
        return mapToDto(saved);
    }

    @Override
    public RoleDto getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Role not found with id: " + id));
        return mapToDto(role);
    }

    @Override
    public List<RoleDto> getAll() {
        return roleRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleDto update(Long id, CreateRoleRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Role not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!role.getName().equals(request.name())) {
            if (roleRepository.findByName(request.name()).isPresent()) {
                throw new ApplicationException("Role with name '" + request.name() + "' already exists");
            }
        }

        role.setName(request.name());
        role.setDescription(request.description());

        // Update permissions if provided
        if (request.permissionIds() != null) {
            Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(request.permissionIds()));
            if (permissions.size() != request.permissionIds().size()) {
                throw new ApplicationException("Some permissions not found");
            }
            role.setPermissions(permissions);
        }

        Role updated = roleRepository.save(role);
        return mapToDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Role not found with id: " + id));
        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public RoleDto assignPermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ApplicationException("Role not found with id: " + roleId));

        Set<Permission> permissions = new HashSet<>(permissionRepository.findAllById(permissionIds));
        if (permissions.size() != permissionIds.size()) {
            throw new ApplicationException("Some permissions not found");
        }

        role.setPermissions(permissions);
        Role updated = roleRepository.save(role);
        return mapToDto(updated);
    }

    private RoleDto mapToDto(Role role) {
        Set<PermissionDto> permissionDtos = role.getPermissions().stream()
                .map(this::mapPermissionToDto)
                .collect(Collectors.toSet());

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .status(role.getStatus())
                .permissions(permissionDtos)
                .build();
    }

    private PermissionDto mapPermissionToDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .module(permission.getModule())
                .description(permission.getDescription())
                .status(permission.getStatus())
                .build();
    }
}

