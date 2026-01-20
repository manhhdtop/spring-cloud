package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank
        String email
) {
    @Override
    public String email() {
        return StringUtils.strip(email);
    }
}
