package info.manhhdtop.cloud.auth.controllers;

import info.manhhdtop.cloud.auth.dtos.requests.AssignRoleToUserRequest;
import info.manhhdtop.cloud.auth.services.UserRoleService;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user-roles")
@RestController
@Slf4j
@Validated
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignRolesToUser(@RequestBody @Valid AssignRoleToUserRequest request) {
        userRoleService.assignRolesToUser(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/users/{userId}/roles")
    public ResponseEntity<?> removeRolesFromUser(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        userRoleService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable Long userId) {
        List<RoleDto> response = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/users/{userId}/available-roles")
    public ResponseEntity<?> getAvailableRoles(@PathVariable Long userId) {
        List<RoleDto> response = userRoleService.getAvailableRoles(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

