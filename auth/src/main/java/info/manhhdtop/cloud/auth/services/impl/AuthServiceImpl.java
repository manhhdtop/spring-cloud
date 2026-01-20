package info.manhhdtop.cloud.auth.services.impl;

import info.manhhdtop.cloud.auth.dtos.requests.ForgotPasswordRequest;
import info.manhhdtop.cloud.auth.dtos.requests.LoginRequest;
import info.manhhdtop.cloud.auth.dtos.requests.RegisterRequest;
import info.manhhdtop.cloud.auth.dtos.requests.ResetPasswordRequest;
import info.manhhdtop.cloud.auth.dtos.responses.LoginDto;
import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.auth.models.RefreshToken;
import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.auth.repositories.RefreshTokenRepository;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.auth.services.AuthService;
import info.manhhdtop.cloud.auth.services.UserEventPublisher;
import info.manhhdtop.cloud.auth.utils.JwtUtil;
import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import info.manhhdtop.cloud.common.core.constants.JwtConstant;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.PermissionDto;
import info.manhhdtop.cloud.common.core.dtos.RoleDto;
import info.manhhdtop.cloud.common.core.dtos.SessionData;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import info.manhhdtop.cloud.common.core.exceptions.UnauthorizedException;
import info.manhhdtop.cloud.common.redis.RedisCredentialService;
import info.manhhdtop.cloud.common.core.utils.EncryptionUtil;
import info.manhhdtop.cloud.common.core.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final RedisCredentialService redisCredentialService;
    private final UserEventPublisher userEventPublisher;

    @Override
    public LoginDto login(LoginRequest request) {
        var user = userRepository.findByEmailWithRolesAndPermissions(request.email())
                .orElseThrow(UnauthorizedException::getInstance);
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw UnauthorizedException.getInstance();
        }
        final var hashPassword = user.getPassword();
        if (!passwordEncoder.matches(request.password(), hashPassword)) {
            throw UnauthorizedException.getInstance();
        }
        var accessTokenDto = JwtUtil.generateAccessToken(user);
        var refreshTokenDto = JwtUtil.generateRefreshToken(user);
        RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String userAgent = httpServletRequest.getHeader(HeaderConstant.USER_AGENT);
        var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        var deviceId = EncryptionUtil.sha256(userAgent);
        refreshToken.setDeviceId(deviceId);
        refreshToken.setUserAgent(userAgent);
        refreshToken.setToken(refreshTokenDto.getToken());
        refreshToken.setExpiredAt(OffsetDateTime.now().plusSeconds(refreshTokenDto.getExpiryAt()));
        refreshTokenRepository.save(refreshToken);

        // Build user DTO
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();

        // Build roles and permissions DTOs
        List<RoleDto> roleDtos = user.getRoles().stream()
                .map(this::mapRoleToDto)
                .collect(Collectors.toList());

        // Collect all unique permissions from all roles
        Set<PermissionDto> permissionDtos = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(this::mapPermissionToDto)
                .collect(Collectors.toSet());

        // Build session data
        String ip = httpServletRequest.getRemoteAddr();
        SessionData sessionData = SessionData.builder()
                .user(userDto)
                .deviceId(deviceId)
                .userAgent(userAgent)
                .ip(ip)
                .roles(roleDtos)
                .permissions(permissionDtos)
                .build();

        // Store user and session data in Redis
        redisCredentialService.storeCredential(accessTokenDto.getToken(), sessionData, accessTokenDto.getExpiryAt());

        return LoginDto.builder()
                .accessToken(accessTokenDto.getToken())
                .accessExpiresIn(accessTokenDto.getExpiryAt())
                .refreshToken(refreshTokenDto.getToken())
                .refreshExpiresIn(refreshTokenDto.getExpiryAt())
                .build();
    }

    private RoleDto mapRoleToDto(Role role) {
        Set<PermissionDto> permissionDtos = role.getPermissions().stream()
                .map(this::mapPermissionToDto)
                .collect(Collectors.toSet());

        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .status(role.getStatus())
                .permissions(permissionDtos)
                .build();
    }

    private PermissionDto mapPermissionToDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .module(permission.getModule())
                .description(permission.getDescription())
                .status(permission.getStatus())
                .build();
    }

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ApplicationException("User with email " + request.email() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        UserDto userDto = JsonUtil.convert(user, UserDto.class);
        // Publish user created event
        userEventPublisher.publishUserCreated(userDto);

        // Build and return UserDto
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .status(user.getStatus())
                .build();
    }

    @Override
    public void logout() {
        RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String token = extractTokenFromRequest(httpServletRequest);
        if (token != null) {
            redisCredentialService.removeCredential(token);
        }
    }

    @Override
    public LoginDto refreshToken() {
        return null;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
    }

    /**
     * Extract JWT token from Authorization header
     * @param request HTTP request
     * @return JWT token or null if not found
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(JwtConstant.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(JwtConstant.BEARER)) {
            return authorizationHeader.substring(JwtConstant.BEARER.length()).trim();
        }
        return null;
    }
}
