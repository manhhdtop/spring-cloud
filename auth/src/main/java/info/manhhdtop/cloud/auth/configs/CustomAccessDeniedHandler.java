package info.manhhdtop.cloud.auth.configs;

import info.manhhdtop.cloud.common.core.dtos.ApiResponse;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull AccessDeniedException exc) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType("application/json");
        response.getWriter().write(JsonUtil.toJson(ApiResponse.error(HttpServletResponse.SC_FORBIDDEN, exc.getMessage())));
    }
}
