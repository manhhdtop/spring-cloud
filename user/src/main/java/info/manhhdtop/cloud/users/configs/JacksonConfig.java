package info.manhhdtop.cloud.users.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JsonUtil.OBJECT_MAPPER;
    }
}

