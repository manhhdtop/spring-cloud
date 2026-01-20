package info.manhhdtop.cloud.users.services;

import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.messaging.events.UserEvent;
import info.manhhdtop.cloud.users.models.User;
import info.manhhdtop.cloud.users.repositories.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventConsumer {
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @RabbitListener(queues = "${spring.rabbitmq.queue.user.sync:" + RabbitMQConstant.User.SYNC_QUEUE + "}")
    @Transactional
    public void handleUserSync(UserEvent event) {
        log.info("Received USER_SYNC event for user: {}", event.getEmail());
        try {
            Optional<User> existingUser = userRepository.findByEmail(event.getEmail());
            User user;
            if (existingUser.isEmpty()) {
                user = new User();
                user.setEmail(event.getEmail());
                user.setName(event.getName());
                user.setAvatar(event.getAvatar());
                user.setGender(event.getGender());
                user.setStatus(UserStatus.ACTIVE);
                user = userRepository.save(user);
                log.info("Created user in user service from sync: {}", event.getEmail());
            } else {
                log.info("User already exists, syncing data: {}", event.getEmail());
                // Sync data from auth service
                user = existingUser.get();
                if (event.getName() != null) {
                    user.setName(event.getName());
                }
                if (event.getAvatar() != null) {
                    user.setAvatar(event.getAvatar());
                }
                if (event.getGender() != null) {
                    user.setGender(event.getGender());
                }
                user.setStatus(UserStatus.ACTIVE);
                user = userRepository.save(user);
            }
            
            // Publish USER_CREATED event with ACTIVE status back to auth service
            userEventPublisher.publishUserCreated(user);
            log.info("Published USER_CREATED event with ACTIVE status for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error handling USER_SYNC event for user: {}", event.getEmail(), e);
            throw e; // Re-throw to trigger retry or dead letter queue
        }
    }
}

