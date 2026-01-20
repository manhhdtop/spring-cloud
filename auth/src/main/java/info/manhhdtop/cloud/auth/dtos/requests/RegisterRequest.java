package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.constants.Gender;
import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name,
        String avatar,
        Gender gender
) {
    @Override
    public String email() {
        return StringUtils.strip(email);
    }

    @Override
    public String password() {
        return StringUtils.strip(password);
    }

    @Override
    public String name() {
        return StringUtils.strip(name);
    }

    @Override
    public String avatar() {
        return StringUtils.strip(avatar);
    }
}
