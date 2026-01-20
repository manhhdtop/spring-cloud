package info.manhhdtop.cloud.auth.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class LoginDto {
    @Builder.Default
    private String tokenType = "Bearer";
    private String accessToken;
    private Long accessExpiresIn;
    private String refreshToken;
    private Long refreshExpiresIn;
}
