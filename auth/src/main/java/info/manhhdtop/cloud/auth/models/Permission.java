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

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SoftDelete(columnName = BaseModel.DELETED)
@SQLRestriction(BaseModel.DELETED_CONDITION)
@Table(name = Permission.TABLE_NAME)
public class Permission extends AuditModel {
    public static final String TABLE_NAME = "permission";

    public static final String NAME = "name";
    public static final String MODULE = "module";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";

    @Column(name = NAME, unique = true, nullable = false)
    private String name;

    @Column(name = MODULE)
    private String module;

    @Column(name = DESCRIPTION)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = STATUS)
    private Status status = Status.ACTIVE;
}

