package info.manhhdtop.cloud.common.core.dtos;

import info.manhhdtop.cloud.common.core.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private String description;
    private Status status;
    private Set<PermissionDto> permissions;
}
