package info.manhhdtop.cloud.auth.utils;

import info.manhhdtop.cloud.auth.configs.JwtPropertyHolder;
import info.manhhdtop.cloud.auth.dtos.responses.JwtData;
import info.manhhdtop.cloud.auth.dtos.responses.TokenDto;
import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.common.core.constants.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public final class JwtUtil {
    private static final String USER_ID = "user-id";
    private static final String EMAIL = "email";
    private static final String SUBJECT = "subject";
    private static final String ISSUED_AT = "iat";
    private static final String TOKEN_TYPE = "type";
    private static final String EXPIRATION = "exp";
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private static long ACCESS_TOKEN_EXPIRY_TIME;
    private static long REFRESH_TOKEN_EXPIRY_TIME;

    public static TokenDto generateAccessToken(User user) {
        return generateToken(user.getId(), user.getEmail(), TokenType.ACCESS, ACCESS_TOKEN_EXPIRY_TIME);
    }

    public static TokenDto generateAccessToken(Long userId, String email) {
        return generateToken(userId, email, TokenType.ACCESS, ACCESS_TOKEN_EXPIRY_TIME);
    }

    public static TokenDto generateRefreshToken(User user) {
        return generateToken(user.getId(), user.getEmail(), TokenType.REFRESH, REFRESH_TOKEN_EXPIRY_TIME);
    }

    public static TokenDto generateRefreshToken(Long userId, String email) {
        return generateToken(userId, email, TokenType.REFRESH, REFRESH_TOKEN_EXPIRY_TIME);
    }

    public static TokenDto generateToken(Long userId, String email, TokenType type, long expiryTime) {
        return generateToken(
                Map.of(
                        USER_ID, userId
                ),
                type,
                expiryTime);
    }

    public static TokenDto generateToken(Map<String, Object> claims, TokenType type, long expiryTime) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiryTime);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .subject((String) claims.get("subject"))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(privateKey, Jwts.SIG.RS512)
                .compact();
        return TokenDto.builder()
                .userId(claims.get(USER_ID) == null ? null : Long.parseLong(claims.get(USER_ID).toString()))
                .token(token)
                .type(type)
                .expiryAt(expiryTime)
                .build();
    }

    public static JwtData parse(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return JwtData.builder()
                .userId(payload.get(USER_ID) == null ? null : Long.parseLong(payload.get(USER_ID).toString()))
                .email(payload.get(EMAIL) == null ? null : payload.get(EMAIL).toString())
                .expiryDate(payload.get(EXPIRATION) == null ? null : new Date(Long.parseLong(payload.get(EXPIRATION).toString())))
                .type(payload.get(TOKEN_TYPE) == null ? null : TokenType.valueOf(payload.get(TOKEN_TYPE).toString()))
                .build();
    }

    public static boolean isTokenValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @PostConstruct
    public void init() throws Exception {
        privateKey = KeyUtil.loadPrivateKey(JwtPropertyHolder.getPrivateKey(), JwtPropertyHolder.isFromFile());
        publicKey = KeyUtil.loadPublicKey(JwtPropertyHolder.getPublicKey(), JwtPropertyHolder.isFromFile());
        ACCESS_TOKEN_EXPIRY_TIME = JwtPropertyHolder.getAccessTokenExpiryTime();
        REFRESH_TOKEN_EXPIRY_TIME = JwtPropertyHolder.getRefreshTokenExpiryTime();
    }
}

