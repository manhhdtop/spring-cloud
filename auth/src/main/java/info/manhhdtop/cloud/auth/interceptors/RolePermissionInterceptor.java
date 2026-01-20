package info.manhhdtop.cloud.auth.interceptors;

import info.manhhdtop.cloud.common.security.annotations.RequirePermission;
import info.manhhdtop.cloud.common.security.annotations.RequireRole;
import info.manhhdtop.cloud.common.core.dtos.RequestContext;
import info.manhhdtop.cloud.common.core.exceptions.UnauthorizedException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor để check role và permission từ annotations @RequireRole và @RequirePermission
 * Sử dụng thông tin từ RequestContext (đã được set bởi GatewayIdentityFilter)
 */
@Component
@Slf4j
public class RolePermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull Object handler) {
        // Chỉ xử lý HandlerMethod (controller methods)
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Lấy roles và permissions từ RequestContext (đã được set bởi GatewayIdentityFilter)
        RequestContext requestContext = RequestContext.get();
        Set<String> userRoles = requestContext.getRoles();
        Set<String> userPermissions = requestContext.getPermissions();

        // Nếu không có user context, có thể là public endpoint
        if ((userRoles == null || userRoles.isEmpty()) && 
            (userPermissions == null || userPermissions.isEmpty())) {
            // Check nếu có annotation yêu cầu role/permission thì throw exception
            if (hasRequireRoleOrPermission(handlerMethod)) {
                log.warn("Access denied: No user context found but endpoint requires role/permission");
                throw UnauthorizedException.getInstance();
            }
            return true;
        }

        // Check class-level annotations
        RequireRole classRole = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequireRole.class);
        if (classRole != null && !hasRole(userRoles, classRole.value())) {
            log.warn("Access denied: User does not have required role: {}", classRole.value());
            throw UnauthorizedException.getInstance();
        }

        RequirePermission classPermission = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequirePermission.class);
        if (classPermission != null && !hasPermission(userPermissions, classPermission.value())) {
            log.warn("Access denied: User does not have required permission: {}", classPermission.value());
            throw UnauthorizedException.getInstance();
        }

        // Check method-level annotations (method annotations override class annotations)
        RequireRole methodRole = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequireRole.class);
        if (methodRole != null && !hasRole(userRoles, methodRole.value())) {
            log.warn("Access denied: User does not have required role: {}", methodRole.value());
            throw UnauthorizedException.getInstance();
        }

        RequirePermission methodPermission = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequirePermission.class);
        if (methodPermission != null && !hasPermission(userPermissions, methodPermission.value())) {
            log.warn("Access denied: User does not have required permission: {}", methodPermission.value());
            throw UnauthorizedException.getInstance();
        }

        return true;
    }

    /**
     * Check if handler method has any RequireRole or RequirePermission annotation
     */
    private boolean hasRequireRoleOrPermission(HandlerMethod handlerMethod) {
        RequireRole classRole = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequireRole.class);
        RequirePermission classPermission = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequirePermission.class);
        RequireRole methodRole = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequireRole.class);
        RequirePermission methodPermission = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), RequirePermission.class);
        
        return classRole != null || classPermission != null || methodRole != null || methodPermission != null;
    }

    /**
     * Check if user has the required role
     */
    private boolean hasRole(Set<String> userRoles, String requiredRole) {
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return userRoles.contains(requiredRole);
    }

    /**
     * Check if user has the required permission
     */
    private boolean hasPermission(Set<String> userPermissions, String requiredPermission) {
        if (userPermissions == null || userPermissions.isEmpty()) {
            return false;
        }
        return userPermissions.contains(requiredPermission);
    }
}

