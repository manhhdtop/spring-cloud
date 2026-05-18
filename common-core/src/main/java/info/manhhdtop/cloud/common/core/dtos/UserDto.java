package info.manhhdtop.cloud.common.core.dtos;

import info.manhhdtop.cloud.common.core.constants.Gender;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String avatar;
    private Gender gender;
    private UserStatus status;
    private boolean requireChangePassword;
}
