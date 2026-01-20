package info.manhhdtop.cloud.auth.dtos.responses;

import info.manhhdtop.cloud.common.core.constants.TokenType;
import lombok.Builder;

import java.util.Date;

@Builder
public record JwtData(
        Long userId,
        String sessionId,
        String email,
        TokenType type,
        Date expiryDate
) {
}
