package info.manhhdtop.cloud.auth.models;

import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.core.dtos.UserDto;
import info.manhhdtop.cloud.common.jpa.models.AuditModel;
import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SoftDelete(columnName = BaseModel.DELETED)
@SQLRestriction(BaseModel.DELETED_CONDITION)
@Table(name = User.TABLE_NAME)
public class User extends AuditModel {
    public static final String TABLE_NAME = "user";
    public static final String USER_ROLE_TABLE = "user_role";

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_EMAIL_VERIFIED = "is_email_verified";
    public static final String STATUS = "status";
    public static final String REQUIRE_CHANGE_PASSWORD = "require_change_password";
    public static final String USER_ID = "user_id";
    public static final String ROLE_ID = "role_id";

    @Column(name = EMAIL)
    private String email;

    @Column(name = PASSWORD)
    private String password;

    @Column(name = IS_EMAIL_VERIFIED)
    private boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = STATUS)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = REQUIRE_CHANGE_PASSWORD)
    private boolean requireChangePassword = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = USER_ROLE_TABLE,
            joinColumns = @JoinColumn(name = USER_ID),
            inverseJoinColumns = @JoinColumn(name = ROLE_ID)
    )
    private Set<Role> roles = new HashSet<>();

    public UserDto toDto() {
        var dto = new UserDto();
        dto.setEmail(email);
        dto.setStatus(status);
        dto.setRequireChangePassword(requireChangePassword);
        return dto;
    }
}
