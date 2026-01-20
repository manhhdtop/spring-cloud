package info.manhhdtop.cloud.auth.controllers;

import info.manhhdtop.cloud.auth.dtos.requests.CreatePermissionRequest;
import info.manhhdtop.cloud.auth.services.PermissionService;
import info.manhhdtop.cloud.common.security.annotations.RequirePermission;
import info.manhhdtop.cloud.common.security.annotations.RequireRole;
import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/permissions")
@RestController
@Slf4j
@Validated
@RequireRole(value = "PERMISSION_ADMIN", description = "Admin role for permission management", module = "permission")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    @RequirePermission(value = "permission.create", description = "Create permission", module = "permission")
    public ResponseEntity<?> create(@RequestBody @Valid CreatePermissionRequest request) {
        PermissionDto response = permissionService.create(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @RequirePermission(value = "permission.read", description = "Read permission", module = "permission")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        PermissionDto response = permissionService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    @RequirePermission(value = "permission.list", description = "List all permissions", module = "permission")
    public ResponseEntity<?> getAll() {
        List<PermissionDto> response = permissionService.getAll();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @RequirePermission(value = "permission.update", description = "Update permission", module = "permission")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid CreatePermissionRequest request) {
        PermissionDto response = permissionService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @RequirePermission(value = "permission.delete", description = "Delete permission", module = "permission")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}

