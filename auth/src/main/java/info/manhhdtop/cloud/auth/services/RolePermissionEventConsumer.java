package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.dtos.requests.CreatePermissionRequest;
import info.manhhdtop.cloud.auth.dtos.requests.CreateRoleRequest;
import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.messaging.events.PermissionEvent;
import info.manhhdtop.cloud.common.messaging.events.RoleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Consumer để nhận events từ các service khác và tạo role/permission trong auth service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionEventConsumer {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @RabbitListener(queues = "${spring.rabbitmq.queue.role.create.request:" + RabbitMQConstant.Role.CREATE_REQUEST_QUEUE + "}")
    @Transactional
    public void handleRoleCreateRequest(RoleEvent event) {
        log.info("Received ROLE_CREATE_REQUEST event for role: {}", event.getRoleName());
        try {
            CreateRoleRequest request = new CreateRoleRequest(
                    event.getRoleName(),
                    event.getDescription() != null ? event.getDescription() : "",
                    event.getPermissionIds()
            );
            roleService.create(request);
            log.info("Successfully created role: {} from event", event.getRoleName());
        } catch (Exception e) {
            log.error("Error handling ROLE_CREATE_REQUEST event for role: {}", event.getRoleName(), e);
            // Có thể thêm retry logic hoặc dead letter queue ở đây
            throw e;
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.permission.create.request:" + RabbitMQConstant.Permission.CREATE_REQUEST_QUEUE + "}")
    @Transactional
    public void handlePermissionCreateRequest(PermissionEvent event) {
        log.info("Received PERMISSION_CREATE_REQUEST event for permission: {}", event.getPermissionName());
        try {
            CreatePermissionRequest request = new CreatePermissionRequest(
                    event.getPermissionName(),
                    event.getModule() != null ? event.getModule() : "",
                    event.getDescription() != null ? event.getDescription() : ""
            );
            permissionService.create(request);
            log.info("Successfully created permission: {} from event", event.getPermissionName());
        } catch (Exception e) {
            log.error("Error handling PERMISSION_CREATE_REQUEST event for permission: {}", 
                    event.getPermissionName(), e);
            // Có thể thêm retry logic hoặc dead letter queue ở đây
            throw e;
        }
    }
}

