package info.manhhdtop.cloud.gateway.exceptions;

import info.manhhdtop.cloud.common.core.utils.ApiResponseFactory;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import info.manhhdtop.cloud.gateway.support.UpstreamConnectionFailures;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Returns HTTP 503 with {@link info.manhhdtop.cloud.common.core.dtos.ApiResponse}
 * when an upstream service cannot be reached (connection refused, no instance, etc.).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public @NonNull Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        if (exchange.getResponse().isCommitted() || !UpstreamConnectionFailures.isUpstreamUnavailable(ex)) {
            return Mono.error(ex);
        }

        String service = UpstreamConnectionFailures.extractServiceName(ex);
        byte[] bytes = Objects.requireNonNull(JsonUtil.toJson(ApiResponseFactory.serviceUnavailable(service)))
                .getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
