package info.manhhdtop.cloud.users.configs;

import info.manhhdtop.cloud.common.core.dtos.RequestContext;
import info.manhhdtop.cloud.common.jpa.providers.OffsetDateTimeProvider;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepositoryFactoryBean;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "offsetDateTimeProvider", auditorAwareRef = "auditorAwareProvider")
@EnableJpaRepositories(
        basePackages = "info.manhhdtop.cloud.users.repositories",
        repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class
)
public class JpaConfig {
    @Bean
    public AuditorAware<@NonNull Long> auditorAwareProvider() {
        return () -> Optional.ofNullable(RequestContext.userId());
    }

    @Bean
    public OffsetDateTimeProvider offsetDateTimeProvider() {
        return new OffsetDateTimeProvider();
    }
}
