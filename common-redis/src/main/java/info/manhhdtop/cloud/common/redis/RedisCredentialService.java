package info.manhhdtop.cloud.common.redis;

import info.manhhdtop.cloud.common.core.dtos.SessionData;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

@RequiredArgsConstructor
@Slf4j
public class RedisCredentialService {
    private static final String CREDENTIAL_PREFIX = "auth:credential:";
    private final StringRedisTemplate redisTemplate;

    public void storeCredential(String token, SessionData sessionData, long expiryInSeconds) {
        String key = CREDENTIAL_PREFIX + token;
        String value = JsonUtil.toJson(sessionData);
        redisTemplate.opsForValue().set(key, value, expiryInSeconds, TimeUnit.SECONDS);
        log.debug("Stored credential in Redis with key: {} and expiry: {} seconds", key, expiryInSeconds);
    }

    public SessionData getCredential(String token) {
        String key = CREDENTIAL_PREFIX + token;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            log.debug("Credential not found in Redis with key: {}", key);
            return null;
        }
        SessionData sessionData = JsonUtil.toObject(value, SessionData.class);
        log.debug("Retrieved credential from Redis with key: {}", key);
        return sessionData;
    }

    public boolean hasCredential(String token) {
        String key = CREDENTIAL_PREFIX + token;
        Boolean exists = redisTemplate.hasKey(key);
        log.debug("Checked credential in Redis with key: {}, exists: {}", key, exists);
        return Boolean.TRUE.equals(exists);
    }

    public void removeCredential(String token) {
        String key = CREDENTIAL_PREFIX + token;
        redisTemplate.delete(key);
        log.debug("Removed credential from Redis with key: {}", key);
    }
}

