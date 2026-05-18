package info.manhhdtop.cloud.common.security.filters;

import info.manhhdtop.cloud.common.core.dtos.RequestContext;
import info.manhhdtop.cloud.common.core.utils.ApiResponseFactory;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import info.manhhdtop.cloud.common.core.utils.RequireChangePasswordPaths;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Blocks authenticated requests when the user must change their password,
 * except for auth endpoints that allow password recovery or session teardown.
 */
public class RequireChangePasswordFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        RequestContext ctx = RequestContext.get();
        if (ctx.getUserId() != null
                && ctx.isRequireChangePassword()
                && !RequireChangePasswordPaths.isAllowed(request.getRequestURI())) {
            writeForbidden(response);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static void writeForbidden(HttpServletResponse response) throws IOException {
        var body = ApiResponseFactory.requireChangePassword();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JsonUtil.toJson(body));
    }
}
