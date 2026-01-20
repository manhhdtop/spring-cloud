package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CreateRoleRequest(
        @NotBlank
        String name,
        String description,
        Set<Long> permissionIds
) {
    @Override
    public String name() {
        return StringUtils.strip(name);
    }

    @Override
    public String description() {
        return StringUtils.strip(description);
    }
}

