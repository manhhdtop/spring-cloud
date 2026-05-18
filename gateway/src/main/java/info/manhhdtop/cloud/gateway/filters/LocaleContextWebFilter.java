package info.manhhdtop.cloud.gateway.filters;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import info.manhhdtop.cloud.common.core.i18n.LocaleSupport;
import java.util.Locale;
import org.jspecify.annotations.NonNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LocaleContextWebFilter implements WebFilter {

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        Locale locale = resolveLocale(exchange.getRequest());
        LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(locale), true);
        return chain.filter(exchange).doFinally(_ -> LocaleContextHolder.resetLocaleContext());
    }

    static Locale resolveLocale(ServerHttpRequest request) {
        String explicit = request.getHeaders().getFirst(HeaderConstant.X_LOCALE);
        if (StringUtils.hasText(explicit)) {
            return LocaleSupport.resolve(explicit);
        }
        String lang = request.getQueryParams().getFirst("lang");
        if (StringUtils.hasText(lang)) {
            return LocaleSupport.resolve(lang);
        }
        return LocaleSupport.resolveFromAcceptLanguage(request.getHeaders().getFirst(HttpHeaders.ACCEPT_LANGUAGE));
    }
}
