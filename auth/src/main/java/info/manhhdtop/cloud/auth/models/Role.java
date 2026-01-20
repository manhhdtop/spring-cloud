package info.manhhdtop.cloud.auth.models;

import info.manhhdtop.cloud.common.core.constants.Status;
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
@Table(name = Role.TABLE_NAME)
public class Role extends AuditModel {
    public static final String TABLE_NAME = "role";
    public static final String ROLE_PERMISSION_TABLE = "role_permission";

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String ROLE_ID = "role_id";
    public static final String PERMISSION_ID = "permission_id";

    @Column(name = NAME, unique = true, nullable = false)
    private String name;

    @Column(name = DESCRIPTION)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = STATUS)
    private Status status = Status.ACTIVE;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = ROLE_PERMISSION_TABLE,
            joinColumns = @JoinColumn(name = ROLE_ID),
            inverseJoinColumns = @JoinColumn(name = PERMISSION_ID)
    )
    private Set<Permission> permissions = new HashSet<>();
}

