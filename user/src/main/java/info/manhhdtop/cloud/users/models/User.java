package info.manhhdtop.cloud.users.models;

import info.manhhdtop.cloud.common.core.constants.Gender;
import info.manhhdtop.cloud.common.core.constants.UserStatus;
import info.manhhdtop.cloud.common.jpa.models.AuditModel;
import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

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
    
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    public static final String AVATAR = "avatar";
    public static final String PHONE = "phone";
    public static final String STATUS = "status";
    public static final String GENDER = "gender";
    public static final String IS_EMAIL_VERIFIED = "is_email_verified";

    @Column(name = EMAIL)
    private String email;
    
    @Column(name = NAME)
    private String name;
    
    @Column(name = AVATAR, columnDefinition = "TINYTEXT")
    private String avatar;
    
    @Column(name = PHONE)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = STATUS)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = GENDER)
    private Gender gender;
}
