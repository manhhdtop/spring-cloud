package info.manhhdtop.cloud.gateway.services;

import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCredentialService {
    private static final String CREDENTIAL_PREFIX = "auth:credential:";

    private final ReactiveStringRedisTemplate redisTemplate;

    /**
     * Get user data from Redis by token
     * @param token The access token
     * @return Mono of UserDto or empty if not found
     */
    public Mono<UserDto> getUserByToken(String token) {
        String key = CREDENTIAL_PREFIX + token;
        return redisTemplate.opsForValue().get(key)
                .mapNotNull(value -> {
                    try {
                        // Create a simple class to hold the session data structure
                        SessionDataWrapper wrapper = JsonUtil.toObject(value, SessionDataWrapper.class);
                        if (wrapper != null && wrapper.user != null) {
                            log.debug("Retrieved user from Redis with key: {}", key);
                            return wrapper.user;
                        }
                    } catch (Exception e) {
                        log.error("Error parsing session data from Redis: {}", e.getMessage());
                    }
                    return null;
                });
    }

    // Inner class to match the JSON structure from auth service
    private static class SessionDataWrapper {
        public UserDto user;
        public String deviceId;
        public String userAgent;
        public String ip;
    }
}

