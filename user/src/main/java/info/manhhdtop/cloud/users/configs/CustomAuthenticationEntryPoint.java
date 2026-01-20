package info.manhhdtop.cloud.users.configs;

import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(ApiResponse.error(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())));
    }
}
