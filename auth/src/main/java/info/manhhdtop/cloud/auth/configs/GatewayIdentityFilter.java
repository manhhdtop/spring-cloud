package info.manhhdtop.cloud.auth.configs;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import info.manhhdtop.cloud.common.core.constants.JwtConstant;
import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import info.manhhdtop.cloud.common.core.dtos.RequestContext;
import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import info.manhhdtop.cloud.common.core.dtos.SessionData;
import info.manhhdtop.cloud.common.redis.RedisCredentialService;
import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Resolves the caller from the access token (Redis session) or from trusted gateway headers,
 * then exposes the same identity to {@link RequestContext} and Spring Security.
 */
@Component
@RequiredArgsConstructor
public class GatewayIdentityFilter extends OncePerRequestFilter {

    private final RedisCredentialService redisCredentialService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            RequestContext ctx = RequestContext.get();

            String token = extractBearerToken(request);
            if (StringUtils.isNotBlank(token)) {
                SessionData sessionData = redisCredentialService.getCredential(token);
                if (sessionData != null && sessionData.getUser() != null) {
                    populateContextFromSession(ctx, sessionData);
                }
            }

            if (ctx.getUserId() == null) {
                populateContextFromGatewayHeaders(request, ctx);
            }

            if (ctx.getUserId() != null) {
                SecurityContextHolder.getContext().setAuthentication(buildAuthentication(ctx));
            }

            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
            RequestContext.clear();
        }
    }

    private static void populateContextFromSession(RequestContext ctx, SessionData sessionData) {
        ctx.setUserId(sessionData.getUser().getId());
        ctx.setRequireChangePassword(sessionData.getUser().isRequireChangePassword());
        ctx.setRoles(toRoleNames(sessionData.getRoles()));
        ctx.setPermissions(toPermissionNames(sessionData.getPermissions()));
    }

    private static void populateContextFromGatewayHeaders(HttpServletRequest request, RequestContext ctx) {
        String userIdHeader = request.getHeader(HeaderConstant.X_USER_ID);
        if (StringUtils.isNotBlank(userIdHeader)) {
            try {
                ctx.setUserId(Long.parseLong(userIdHeader.strip()));
            } catch (NumberFormatException ignored) {
                // ignore malformed header
            }
        }
        String sessionIdHeader = request.getHeader(HeaderConstant.X_SESSION_ID);
        if (StringUtils.isNotBlank(sessionIdHeader)) {
            try {
                ctx.setSessionId(Long.parseLong(sessionIdHeader.strip()));
            } catch (NumberFormatException ignored) {
                // ignore
            }
        }
        String requestId = request.getHeader(HeaderConstant.X_REQUEST_ID);
        ctx.setRequestId(StringUtils.strip(requestId));
    }

    private static Set<String> toRoleNames(List<RoleDto> roles) {
        if (roles == null || roles.isEmpty()) {
            return new HashSet<>();
        }
        return roles.stream()
                .map(RoleDto::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static Set<String> toPermissionNames(Set<PermissionDto> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return new HashSet<>();
        }
        return permissions.stream()
                .map(PermissionDto::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(HashSet::new));
    }

    private static UsernamePasswordAuthenticationToken buildAuthentication(RequestContext ctx) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (ctx.getRoles() != null) {
            for (String role : ctx.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        if (ctx.getPermissions() != null) {
            for (String permission : ctx.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority(permission));
            }
        }
        return new UsernamePasswordAuthenticationToken(ctx.getUserId(), "", authorities);
    }

    private static String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(JwtConstant.AUTHORIZATION);
        if (header == null) {
            header = request.getHeader(HeaderConstant.AUTHORIZATION);
        }
        if (StringUtils.isNotBlank(header) && StringUtils.startsWith(HeaderConstant.TOKEN_PREFIX, header)) {
            return header.substring(HeaderConstant.TOKEN_PREFIX.length()).strip();
        }
        return null;
    }
}
