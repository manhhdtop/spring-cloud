package info.manhhdtop.cloud.auth.dtos.requests;

import info.manhhdtop.cloud.common.core.utils.StringUtils;
import jakarta.validation.constraints.NotBlank;

public record CreatePermissionRequest(
        @NotBlank
        String name,
        String module,
        String description
) {
    @Override
    public String name() {
        return StringUtils.strip(name);
    }

    @Override
    public String module() {
        return StringUtils.strip(module);
    }

    @Override
    public String description() {
        return StringUtils.strip(description);
    }
}

