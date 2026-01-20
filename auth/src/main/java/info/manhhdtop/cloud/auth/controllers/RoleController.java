package info.manhhdtop.cloud.auth.controllers;

import info.manhhdtop.cloud.auth.dtos.requests.CreateRoleRequest;
import info.manhhdtop.cloud.auth.services.RoleService;
import info.manhhdtop.cloud.common.security.annotations.RequirePermission;
import info.manhhdtop.cloud.common.security.annotations.RequireRole;
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
@RequestMapping("/api/v1/roles")
@RestController
@Slf4j
@Validated
@RequireRole(value = "ROLE_ADMIN", description = "Admin role for role management", module = "role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    @RequirePermission(value = "role.create", description = "Create role", module = "role")
    public ResponseEntity<?> create(@RequestBody @Valid CreateRoleRequest request) {
        RoleDto response = roleService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @RequirePermission(value = "role.read", description = "Read role", module = "role")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        RoleDto response = roleService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @RequirePermission(value = "role.list", description = "List all roles", module = "role")
    public ResponseEntity<?> getAll() {
        List<RoleDto> response = roleService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @RequirePermission(value = "role.update", description = "Update role", module = "role")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid CreateRoleRequest request) {
        RoleDto response = roleService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(value = "role.delete", description = "Delete role", module = "role")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/{id}/permissions")
    @RequirePermission(value = "role.assign.permissions", description = "Assign permissions to role", module = "role")
    public ResponseEntity<?> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        RoleDto response = roleService.assignPermissions(id, permissionIds);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

