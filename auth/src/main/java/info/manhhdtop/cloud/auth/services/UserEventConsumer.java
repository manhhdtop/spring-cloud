package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.common.messaging.constants.RabbitMQConstant;
import info.manhhdtop.cloud.common.messaging.events.UserEvent;
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

    @RabbitListener(queues = "${spring.rabbitmq.queue.user.created:" + RabbitMQConstant.User.CREATED_QUEUE + "}")
    @Transactional
    public void handleUserCreated(UserEvent event) {
        log.info("Received USER_CREATED event from user service for user: {} with status: {}",
                event.getEmail(), event.getStatus());
        try {
            userRepository.findById(event.getId())
                    .ifPresentOrElse(user -> {
                                // Update user status from user service (should be ACTIVE after user service processes)
                                if (user.getStatus() != event.getStatus()) {
                                    log.info("Updated user status in auth service: {} from {} to {}",
                                            event.getEmail(), user.getStatus(), event.getStatus());
                                    user.setStatus(event.getStatus());
                                    userRepository.save(user);
                                } else {
                                    log.debug("User status already matches: {}", event.getEmail());
                                }
                            },
                            () -> log.warn("User not found in auth service for update: {}", event.getId())
                    );
        } catch (Exception e) {
            log.error("Error handling USER_CREATED event from user service for user: {}", event.getEmail(), e);
            throw e;
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.user.updated:" + RabbitMQConstant.User.UPDATED_QUEUE + "}")
    @Transactional
    public void handleUserUpdated(UserEvent event) {
        log.info("Received USER_UPDATED event from user service for user: {}", event.getEmail());
        try {
            userRepository.findById(event.getId())
                    .ifPresentOrElse(
                            user -> {
                                // Update user status from user service
                                if (user.getStatus() != event.getStatus()) {
                                    user.setStatus(event.getStatus());
                                    userRepository.save(user);
                                    log.info("Updated user status in auth service: {} to {}",
                                            event.getEmail(), event.getStatus());
                                } else {
                                    log.debug("User status already matches: {}", event.getEmail());
                                }
                            },
                            () -> log.warn("User not found in auth service for update: {}", event.getId())
                    );
        } catch (Exception e) {
            log.error("Error handling USER_UPDATED event from user service for user: {}", event.getEmail(), e);
            throw e;
        }
    }
}

