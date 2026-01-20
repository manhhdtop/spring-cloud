package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.messaging.events.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishUserSync(UserDto user) {
        if (user == null) {
            return;
        }
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_SYNC)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.SYNC_ROUTING_KEY);
        log.info("Published USER_SYNC event for user: {}", user.getEmail());
    }

    public void publishUserCreated(UserDto user) {
        if (user == null) {
            return;
        }
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_CREATED)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.CREATED_ROUTING_KEY);
        log.info("Published USER_CREATED event for user: {}", user.getEmail());
    }

    public void publishUserUpdated(User user) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_UPDATED)
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.UPDATED_ROUTING_KEY);
        log.info("Published USER_UPDATED event for user: {}", user.getEmail());
    }

    public void publishUserLocked(User user, String reason) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_LOCKED)
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .reason(reason)
                .build();
        publishEvent(event, RabbitMQConstant.User.LOCKED_ROUTING_KEY);
        log.info("Published USER_LOCKED event for user: {}", user.getEmail());
    }

    public void publishUserUnlocked(User user) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_UNLOCKED)
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.UNLOCKED_ROUTING_KEY);
        log.info("Published USER_UNLOCKED event for user: {}", user.getEmail());
    }

    public void publishUserDeleted(User user) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_DELETED)
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.DELETED_ROUTING_KEY);
        log.info("Published USER_DELETED event for user: {}", user.getEmail());
    }

    public void publishUserStatusChanged(User user, UserStatus oldStatus, UserStatus newStatus) {
        UserEvent event = UserEvent.builder()
                .eventType(UserEvent.EventType.USER_STATUS_CHANGED)
                .id(user.getId())
                .email(user.getEmail())
                .status(newStatus)
                .timestamp(OffsetDateTime.now())
                .reason("Status changed from " + oldStatus + " to " + newStatus)
                .build();
        publishEvent(event, RabbitMQConstant.User.STATUS_CHANGED_ROUTING_KEY);
        log.info("Published USER_STATUS_CHANGED event for user: {} from {} to {}",
                user.getEmail(), oldStatus, newStatus);
    }

    private void publishEvent(UserEvent event, String routingKey) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConstant.User.EXCHANGE, routingKey, event);
        } catch (Exception e) {
            log.error("Failed to publish event: {} for user: {}", event.getEventType(), event.getEmail(), e);
            // Có thể thêm retry logic hoặc dead letter queue ở đây
        }
    }
}

