package info.manhhdtop.cloud.users.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonUtil.OBJECT_MAPPER;
    }
}
