package info.manhhdtop.cloud.gateway.configs;

import info.manhhdtop.cloud.common.core.constants.ServiceConstant;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayFilter {
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Auth service routes
                .route(ServiceConstant.Auth.MODULE_NAME.toLowerCase(), r -> r
                        .path(ServiceConstant.Auth.BASE_URL + "**")
                        .uri(ServiceConstant.Auth.URL_LOAD_BALANCE)
                )
                // Auth service Swagger UI routes
                .route("auth-swagger-ui", r -> r
                        .path("/swagger-ui/auth/**")
                        .filters(f -> f.rewritePath("/swagger-ui/auth/(?<segment>.*)", "/swagger-ui/$\\{segment}"))
                        .uri(ServiceConstant.Auth.URL_LOAD_BALANCE)
                )
                .route("auth-swagger-ui-index", r -> r
                        .path("/swagger-ui/auth.html")
                        .filters(f -> f.rewritePath("/swagger-ui/auth.html", "/swagger-ui.html"))
                        .uri(ServiceConstant.Auth.URL_LOAD_BALANCE)
                )
                // Auth service API docs routes
                .route("auth-api-docs", r -> r
                        .path("/v3/api-docs/auth/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/auth/(?<segment>.*)", "/v3/api-docs/$\\{segment}"))
                        .uri(ServiceConstant.Auth.URL_LOAD_BALANCE)
                )
                // User service routes
                .route(ServiceConstant.User.MODULE_NAME.toLowerCase(), r -> r
                        .path(ServiceConstant.User.BASE_URL + "**")
                        .uri(ServiceConstant.User.URL_LOAD_BALANCE)
                )
                // User service Swagger UI routes
                .route("user-swagger-ui", r -> r
                        .path("/swagger-ui/user/**")
                        .filters(f -> f.rewritePath("/swagger-ui/user/(?<segment>.*)", "/swagger-ui/$\\{segment}"))
                        .uri(ServiceConstant.User.URL_LOAD_BALANCE)
                )
                .route("user-swagger-ui-index", r -> r
                        .path("/swagger-ui/user.html")
                        .filters(f -> f.rewritePath("/swagger-ui/user.html", "/swagger-ui.html"))
                        .uri(ServiceConstant.User.URL_LOAD_BALANCE)
                )
                // User service API docs routes
                .route("user-api-docs", r -> r
                        .path("/v3/api-docs/user/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/user/(?<segment>.*)", "/v3/api-docs/$\\{segment}"))
                        .uri(ServiceConstant.User.URL_LOAD_BALANCE)
                )
                .build();
    }
}
