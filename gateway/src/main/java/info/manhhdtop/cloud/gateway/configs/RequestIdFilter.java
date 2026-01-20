package info.manhhdtop.cloud.gateway.configs;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import jakarta.annotation.Nonnull;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RequestIdFilter implements GlobalFilter, Ordered {

    @Override
    @Nonnull
    public Mono<Void> filter(ServerWebExchange exchange, @Nonnull GatewayFilterChain chain) {
        String traceId = exchange.getRequest()
                .getHeaders()
                .getFirst(HeaderConstant.X_REQUEST_ID);

        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .header(HeaderConstant.X_REQUEST_ID, traceId)
                .build();

        MDC.put(HeaderConstant.X_REQUEST_ID, traceId);

        return chain.filter(
                exchange.mutate()
                        .request(request)
                        .build()
        ).doFinally(signal -> MDC.remove(HeaderConstant.X_REQUEST_ID));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
