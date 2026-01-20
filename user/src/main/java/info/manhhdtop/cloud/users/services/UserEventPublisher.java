package info.manhhdtop.cloud.users.services;

import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.messaging.events.UserEvent;
import info.manhhdtop.cloud.users.models.User;
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

    public void publishUserCreated(User user) {
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
                .name(user.getName())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .status(user.getStatus())
                .timestamp(OffsetDateTime.now())
                .build();
        publishEvent(event, RabbitMQConstant.User.UPDATED_ROUTING_KEY);
        log.info("Published USER_UPDATED event for user: {}", user.getEmail());
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

