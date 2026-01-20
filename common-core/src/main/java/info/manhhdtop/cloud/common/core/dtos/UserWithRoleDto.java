package info.manhhdtop.cloud.common.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@AllArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserWithRoleDto extends UserDto {
    private Set<RoleDto> roles;
}
