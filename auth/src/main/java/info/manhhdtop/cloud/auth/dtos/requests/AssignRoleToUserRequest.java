package info.manhhdtop.cloud.auth.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AssignRoleToUserRequest(
        @NotNull
        Long userId,
        @NotEmpty
        Set<Long> roleIds
) {
}

