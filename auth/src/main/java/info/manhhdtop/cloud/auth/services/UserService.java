package info.manhhdtop.cloud.auth.services;

import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public void lockUser(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found with id: " + userId));
        
        user.setStatus(UserStatus.LOCKED);
        userRepository.save(user);
        
        userEventPublisher.publishUserLocked(user, reason);
        log.info("User locked: {} - Reason: {}", user.getEmail(), reason);
    }

    @Transactional
    public void unlockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found with id: " + userId));
        
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        
        userEventPublisher.publishUserUnlocked(user);
        log.info("User unlocked: {}", user.getEmail());
    }

    @Transactional
    public void changeUserStatus(Long userId, UserStatus newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found with id: " + userId));
        
        UserStatus oldStatus = user.getStatus();
        if (oldStatus != newStatus) {
            user.setStatus(newStatus);
            userRepository.save(user);
            
            userEventPublisher.publishUserStatusChanged(user, oldStatus, newStatus);
            log.info("User status changed: {} from {} to {}", user.getEmail(), oldStatus, newStatus);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException("User not found with id: " + userId));
        
        userRepository.delete(user);
        userEventPublisher.publishUserDeleted(user);
        log.info("User deleted: {}", user.getEmail());
    }
}

