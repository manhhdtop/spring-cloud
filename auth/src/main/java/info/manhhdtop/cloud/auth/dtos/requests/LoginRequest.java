package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
    @Override
    public String email() {
        return StringUtils.strip(email);
    }

    @Override
    public String password() {
        return StringUtils.strip(password);
    }
}
