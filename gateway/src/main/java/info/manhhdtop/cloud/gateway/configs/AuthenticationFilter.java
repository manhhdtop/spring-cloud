package info.manhhdtop.cloud.gateway.configs;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.core.exceptions.UnauthorizedException;
import info.manhhdtop.cloud.common.core.utils.ApiResponseFactory;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import info.manhhdtop.cloud.common.core.utils.RequireChangePasswordPaths;
import info.manhhdtop.cloud.common.core.utils.StringUtils;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import info.manhhdtop.cloud.gateway.services.RedisCredentialService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {
    private final RedisCredentialService redisCredentialService;

    @Override
    @NonNull
    public Mono<@NonNull Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
        // Bypass authentication for Swagger endpoints
        String path = exchange.getRequest().getURI().getPath();
        if (isSwaggerEndpoint(path)) {
            return chain.filter(exchange);
        }
        
        String token = extractToken(exchange);
        if (token == null) {
            return chain.filter(exchange);
        }

        return redisCredentialService.getUserByToken(token)
                .switchIfEmpty(Mono.error(UnauthorizedException::getInstance))
                .flatMap((UserDto user) -> {
                    if (user.isRequireChangePassword() && !RequireChangePasswordPaths.isAllowed(path)) {
                        return writeRequireChangePasswordResponse(exchange);
                    }
                    return exchange.getSession()
                            .flatMap(session -> {
                                ServerHttpRequest request = exchange.getRequest()
                                        .mutate()
                                        .header(HeaderConstant.X_USER_ID, user.getId().toString())
                                        .header(HeaderConstant.X_SESSION_ID, session.getId())
                                        .build();

                                return chain.filter(exchange.mutate().request(request).build());
                            });
                })
                .onErrorResume(UnauthorizedException.class, _ -> {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private String extractToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst(HeaderConstant.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && StringUtils.startsWith(HeaderConstant.TOKEN_PREFIX, header)) {
            return header.substring(HeaderConstant.TOKEN_PREFIX.length()).strip();
        }
        return null;
    }

    private static Mono<Void> writeRequireChangePasswordResponse(ServerWebExchange exchange) {
        byte[] bytes = Objects.requireNonNull(JsonUtil.toJson(ApiResponseFactory.requireChangePassword()))
                .getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private boolean isSwaggerEndpoint(String path) {
        return path != null && (
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui.html") ||
                path.contains("/swagger") ||
                path.contains("/api-docs")
        );
    }
}

