package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.utils.StringUtils;
import info.manhhdtop.cloud.common.core.validations.NotSameValue;
import jakarta.validation.constraints.NotBlank;

@NotSameValue(key1 = "oldPassword", key2 = "newPassword")
public record ChangePasswordRequest(
        @NotBlank
        String email,
        @NotBlank
        String oldPassword,
        @NotBlank
        String newPassword
) {
    @Override
    public String email() {
        return StringUtils.strip(email);
    }

    @Override
    public String oldPassword() {
        return StringUtils.strip(oldPassword);
    }

    @Override
    public String newPassword() {
        return StringUtils.strip(newPassword);
    }
}
