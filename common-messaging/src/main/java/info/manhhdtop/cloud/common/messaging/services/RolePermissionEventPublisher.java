package info.manhhdtop.cloud.common.messaging.services;

import java.util.Set;

/**
 * Interface để publish events tạo role/permission từ các service khác đến auth service
 * Implementation sẽ được tạo trong từng service cần sử dụng
 */
public interface RolePermissionEventPublisher {
    /**
     * Publish event để yêu cầu tạo role trong auth service
     */
    void publishRoleCreateRequest(String roleName, String description, Set<Long> permissionIds);

    /**
     * Publish event để yêu cầu tạo permission trong auth service
     */
    void publishPermissionCreateRequest(String permissionName, String module, String description);
}

