package info.manhhdtop.cloud.auth.configs;

import info.manhhdtop.cloud.common.redis.RedisCredentialService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisCredentialConfig {

    @Bean
    public RedisCredentialService redisCredentialService(StringRedisTemplate stringRedisTemplate) {
        return new RedisCredentialService(stringRedisTemplate);
    }
}
