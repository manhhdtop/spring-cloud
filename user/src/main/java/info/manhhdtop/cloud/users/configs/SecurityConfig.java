package info.manhhdtop.cloud.users.configs;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final GatewayIdentityFilter gatewayIdentityFilter;

    private static Customizer<CorsConfigurer<HttpSecurity>> configureCors() {
        return cors -> cors.configurationSource(_ -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOriginPatterns(List.of("*"));
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowedMethods(List.of("*"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setAllowCredentials(false);
            return configuration;
        });
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.authorizeHttpRequests(httpRequest ->
                httpRequest
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs"
                        ).permitAll()
                        .anyRequest().authenticated()
        );
        httpSecurity.cors(configureCors());
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(gatewayIdentityFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling(ex -> ex
                .authenticationEntryPoint(customAuthenticationEntryPoint)  // 401
                .accessDeniedHandler(customAccessDeniedHandler)  // 403
        );
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
