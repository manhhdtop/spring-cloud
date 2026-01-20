package info.manhhdtop.cloud.auth.configs;

import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.auth.services.UserEventPublisher;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class UserConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;
    private boolean initialized = false;

    @Value("${app.admin.email:admin@cloud.org}")
    private String adminEmail;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Chỉ chạy một lần khi context được refresh
        if (initialized) {
            return;
        }
        initialized = true;

        initAdminUser();
    }

    private void initAdminUser() {
        // Kiểm tra xem user đã tồn tại chưa
        var existingUserOpt = userRepository.findByEmail(adminEmail);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            // Nếu user status là INIT, re-publish sync event
            if (existingUser.getStatus() == UserStatus.INIT) {
                UserDto userDto = existingUser.toDto();
                userDto.setName("Admin");
                log.info("Admin user with email {} exists with INIT status, re-publishing sync event", adminEmail);
                userEventPublisher.publishUserSync(userDto);
            } else {
                log.info("Admin user with email {} already exists with status {}, skipping initialization",
                        adminEmail, existingUser.getStatus());
            }
            return;
        }

        // Tạo password random
        String randomPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(randomPassword);

        // Tạo admin user với status INIT (chưa có role, sẽ được gán sau khi role được tạo)
        User adminUser = new User();
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(encodedPassword);
        adminUser.setStatus(UserStatus.INIT);
        adminUser.setEmailVerified(true);

        adminUser = userRepository.save(adminUser);

        // Publish user sync event to user service
        UserDto userDto = adminUser.toDto();
        userDto.setName("Admin");
        userEventPublisher.publishUserSync(userDto);

        log.info("Admin user initialized successfully with email: {} and status INIT", adminEmail);
        log.info("Random password generated for admin user. Please change it after first login.");
        log.warn("ADMIN PASSWORD: {}", randomPassword);
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
