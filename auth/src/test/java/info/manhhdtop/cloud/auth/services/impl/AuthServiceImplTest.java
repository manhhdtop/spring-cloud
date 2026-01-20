package info.manhhdtop.cloud.auth.services.impl;

import info.manhhdtop.cloud.auth.dtos.requests.LoginRequest;
import info.manhhdtop.cloud.auth.dtos.requests.RegisterRequest;
import info.manhhdtop.cloud.auth.dtos.responses.LoginDto;
import info.manhhdtop.cloud.auth.dtos.responses.TokenDto;
import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.auth.models.RefreshToken;
import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.auth.repositories.RefreshTokenRepository;
import info.manhhdtop.cloud.auth.repositories.UserRepository;
import info.manhhdtop.cloud.auth.services.UserEventPublisher;
import info.manhhdtop.cloud.auth.utils.JwtUtil;
import info.manhhdtop.cloud.common.core.constants.Status;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.core.exceptions.ApplicationException;
import info.manhhdtop.cloud.common.core.exceptions.UnauthorizedException;
import info.manhhdtop.cloud.common.redis.RedisCredentialService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisCredentialService redisCredentialService;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("$2a$10$encodedPasswordHash");
        testUser.setStatus(UserStatus.ACTIVE);

        // Setup test role and permission
        Permission testPermission = new Permission();
        testPermission.setId(1L);
        testPermission.setName("user.read");
        testPermission.setModule("user");
        testPermission.setDescription("Read user");
        testPermission.setStatus(Status.ACTIVE);

        Role testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");
        testRole.setDescription("User role");
        testRole.setStatus(Status.ACTIVE);
        testRole.setPermissions(Set.of(testPermission));

        testUser.setRoles(Set.of(testRole));

        // Setup login request
        validLoginRequest = new LoginRequest("test@example.com", "password123");
    }

    @Test
    @DisplayName("Should return LoginDto when login is successful")
    void testLogin_Success() {
        // Given
        when(userRepository.findByEmailWithRolesAndPermissions(validLoginRequest.email()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(validLoginRequest.password(), testUser.getPassword()))
                .thenReturn(true);

        // Mock JWT token generation
        TokenDto accessTokenDto = TokenDto.builder()
                .token("access-token-123")
                .expiryAt(3600L)
                .build();

        TokenDto refreshTokenDto = TokenDto.builder()
                .token("refresh-token-456")
                .expiryAt(86400L)
                .build();

        // Mock RequestContextHolder
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        RequestAttributes requestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            jwtUtilMock.when(() -> JwtUtil.generateAccessToken(testUser)).thenReturn(accessTokenDto);
            jwtUtilMock.when(() -> JwtUtil.generateRefreshToken(testUser)).thenReturn(refreshTokenDto);

            when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
                RefreshToken token = invocation.getArgument(0);
                token.setId(1L);
                return token;
            });

            // When
            LoginDto result = authService.login(validLoginRequest);

            // Then
            assertNotNull(result);
            assertEquals("access-token-123", result.getAccessToken());
            assertEquals(3600L, result.getAccessExpiresIn());
            assertEquals("refresh-token-456", result.getRefreshToken());
            assertEquals(86400L, result.getRefreshExpiresIn());

            // Verify interactions
            verify(userRepository).findByEmailWithRolesAndPermissions(validLoginRequest.email());
            verify(passwordEncoder).matches(validLoginRequest.password(), testUser.getPassword());
            verify(refreshTokenRepository).save(any(RefreshToken.class));
            verify(redisCredentialService).storeCredential(eq("access-token-123"), any(), eq(3600L));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when user not found")
    void testLogin_UserNotFound() {
        // Given
        when(userRepository.findByEmailWithRolesAndPermissions(validLoginRequest.email()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UnauthorizedException.class, () -> authService.login(validLoginRequest));

        verify(userRepository).findByEmailWithRolesAndPermissions(validLoginRequest.email());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when password is incorrect")
    void testLogin_IncorrectPassword() {
        // Given
        when(userRepository.findByEmailWithRolesAndPermissions(validLoginRequest.email()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(validLoginRequest.password(), testUser.getPassword()))
                .thenReturn(false);

        // When & Then
        assertThrows(UnauthorizedException.class, () -> authService.login(validLoginRequest));

        verify(userRepository).findByEmailWithRolesAndPermissions(validLoginRequest.email());
        verify(passwordEncoder).matches(validLoginRequest.password(), testUser.getPassword());
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create user and publish event when register is successful")
    void testRegister_Success() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("newuser@example.com", "password123", "new user", null, null);
        when(userRepository.findByEmail(registerRequest.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        // When
        UserDto result = authService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals(UserStatus.ACTIVE, result.getStatus());

        verify(userRepository).findByEmail(registerRequest.email());
        verify(passwordEncoder).encode(registerRequest.password());
        verify(userRepository).save(any(User.class));
        verify(userEventPublisher).publishUserCreated(any(UserDto.class));
    }

    @Test
    @DisplayName("Should throw ApplicationException when user already exists")
    void testRegister_UserAlreadyExists() {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("existing@example.com", "password123", "exist", null, null);
        when(userRepository.findByEmail(registerRequest.email()))
                .thenReturn(Optional.of(testUser));

        // When & Then
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> authService.register(registerRequest));

        assertTrue(exception.getMessage().contains("already exists"));

        verify(userRepository).findByEmail(registerRequest.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(userEventPublisher, never()).publishUserCreated(any(UserDto.class));
    }

    @Test
    @DisplayName("Should remove credential from Redis when logout")
    void testLogout_Success() {
        // Given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn("Bearer access-token-123");

        RequestAttributes requestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        try {
            // When
            authService.logout();

            // Then
            verify(redisCredentialService).removeCredential("access-token-123");
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    @DisplayName("Should handle logout when token is not present")
    void testLogout_NoToken() {
        // Given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("Authorization")).thenReturn(null);

        RequestAttributes requestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        try {
            // When
            authService.logout();

            // Then
            verify(redisCredentialService, never()).removeCredential(anyString());
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

    @Test
    @DisplayName("Should handle login with user having no roles")
    void testLogin_UserWithNoRoles() {
        // Given
        User userWithoutRoles = new User();
        userWithoutRoles.setId(1L);
        userWithoutRoles.setEmail("test@example.com");
        userWithoutRoles.setPassword("$2a$10$encodedPasswordHash");
        userWithoutRoles.setStatus(UserStatus.ACTIVE);
        userWithoutRoles.setRoles(new HashSet<>());

        when(userRepository.findByEmailWithRolesAndPermissions(validLoginRequest.email()))
                .thenReturn(Optional.of(userWithoutRoles));
        when(passwordEncoder.matches(validLoginRequest.password(), userWithoutRoles.getPassword()))
                .thenReturn(true);

        TokenDto accessTokenDto = TokenDto.builder()
                .token("access-token-123")
                .expiryAt(3600L)
                .build();

        TokenDto refreshTokenDto = TokenDto.builder()
                .token("refresh-token-456")
                .expiryAt(86400L)
                .build();

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        RequestAttributes requestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            jwtUtilMock.when(() -> JwtUtil.generateAccessToken(userWithoutRoles)).thenReturn(accessTokenDto);
            jwtUtilMock.when(() -> JwtUtil.generateRefreshToken(userWithoutRoles)).thenReturn(refreshTokenDto);

            when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(invocation -> {
                RefreshToken token = invocation.getArgument(0);
                token.setId(1L);
                return token;
            });

            // When
            LoginDto result = authService.login(validLoginRequest);

            // Then
            assertNotNull(result);
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }
}

