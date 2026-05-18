package info.manhhdtop.cloud.common.core.configs;

import info.manhhdtop.cloud.common.core.i18n.LocaleContextFilter;
import info.manhhdtop.cloud.common.core.utils.MessageUtil;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@ConditionalOnClass(Filter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class I18nAutoConfiguration {

    static {
        MessageUtil.init();
    }

    @Bean
    public FilterRegistrationBean<LocaleContextFilter> localeContextFilter() {
        FilterRegistrationBean<LocaleContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LocaleContextFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
