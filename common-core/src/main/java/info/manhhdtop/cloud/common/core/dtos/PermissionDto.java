package info.manhhdtop.cloud.common.core.dtos;

import info.manhhdtop.cloud.common.core.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private String name;
    private String module;
    private String description;
    private Status status;
}
