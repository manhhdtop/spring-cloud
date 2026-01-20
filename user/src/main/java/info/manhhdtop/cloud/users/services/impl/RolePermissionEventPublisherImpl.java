package info.manhhdtop.cloud.users.services.impl;

import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.messaging.events.PermissionEvent;
import info.manhhdtop.cloud.common.messaging.events.RoleEvent;
import info.manhhdtop.cloud.common.messaging.services.RolePermissionEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Set;

/**
 * Implementation của RolePermissionEventPublisher trong user service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionEventPublisherImpl implements RolePermissionEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishRoleCreateRequest(String roleName, String description, Set<Long> permissionIds) {
        RoleEvent event = RoleEvent.builder()
                .eventType(RoleEvent.EventType.ROLE_CREATE_REQUEST)
                .roleName(roleName)
                .description(description)
                .permissionIds(permissionIds)
                .timestamp(OffsetDateTime.now())
                .build();
        publishRoleEvent(event, RabbitMQConstant.Role.CREATE_REQUEST_ROUTING_KEY);
        log.info("Published ROLE_CREATE_REQUEST event for role: {}", roleName);
    }

    @Override
    public void publishPermissionCreateRequest(String permissionName, String module, String description) {
        PermissionEvent event = PermissionEvent.builder()
                .eventType(PermissionEvent.EventType.PERMISSION_CREATE_REQUEST)
                .permissionName(permissionName)
                .module(module)
                .description(description)
                .timestamp(OffsetDateTime.now())
                .build();
        publishPermissionEvent(event, RabbitMQConstant.Permission.CREATE_REQUEST_ROUTING_KEY);
        log.info("Published PERMISSION_CREATE_REQUEST event for permission: {}", permissionName);
    }

    private void publishRoleEvent(RoleEvent event, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstant.Role.EXCHANGE, routingKey, event);
        } catch (Exception e) {
            log.error("Failed to publish role event: {} for role: {}", event.getEventType(), event.getRoleName(), e);
        }
    }

    private void publishPermissionEvent(PermissionEvent event, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstant.Permission.EXCHANGE, routingKey, event);
        } catch (Exception e) {
            log.error("Failed to publish permission event: {} for permission: {}", 
                    event.getEventType(), event.getPermissionName(), e);
        }
    }
}

