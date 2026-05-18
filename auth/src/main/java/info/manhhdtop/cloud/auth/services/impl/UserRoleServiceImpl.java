package info.manhhdtop.cloud.auth.services.impl;

import info.manhhdtop.cloud.auth.dtos.requests.AssignRoleToUserRequest;
import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.auth.repositories.RoleRepository;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.auth.services.UserRoleService;
import info.manhhdtop.cloud.common.core.constants.MessageKeys;
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
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void assignRolesToUser(AssignRoleToUserRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ApplicationException(MessageKeys.USER_NOT_FOUND_BY_ID, request.userId()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
        if (roles.size() != request.roleIds().size()) {
            throw new ApplicationException(MessageKeys.ROLES_NOT_FOUND);
        }

        // Merge with existing roles
        user.getRoles().addAll(roles);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRolesFromUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(MessageKeys.USER_NOT_FOUND_BY_ID, userId));

        Set<Role> rolesToRemove = new HashSet<>(roleRepository.findAllById(roleIds));
        user.getRoles().removeAll(rolesToRemove);
        userRepository.save(user);
    }

    @Override
    public List<RoleDto> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(MessageKeys.USER_NOT_FOUND_BY_ID, userId));

        return user.getRoles().stream()
                .map(this::mapRoleToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDto> getAvailableRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(MessageKeys.USER_NOT_FOUND_BY_ID, userId));

        Set<Long> userRoleIds = user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        return roleRepository.findAll().stream()
                .filter(role -> !userRoleIds.contains(role.getId()))
                .map(this::mapRoleToDto)
                .collect(Collectors.toList());
    }

    private RoleDto mapRoleToDto(Role role) {
        Set<PermissionDto> permissionDtos = role.getPermissions().stream()
                .map(permission -> PermissionDto.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .module(permission.getModule())
                        .description(permission.getDescription())
                        .status(permission.getStatus())
                        .build())
                .collect(Collectors.toSet());

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .status(role.getStatus())
                .permissions(permissionDtos)
                .build();
    }
}

