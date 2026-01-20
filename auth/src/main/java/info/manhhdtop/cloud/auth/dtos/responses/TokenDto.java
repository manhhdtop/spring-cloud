package info.manhhdtop.cloud.auth.dtos.responses;

import info.manhhdtop.cloud.common.core.constants.TokenType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class TokenDto {
    private Long userId;
    private String token;
    private TokenType type;
    private Long expiryAt;
}
