package info.manhhdtop.cloud.auth.models;

import info.manhhdtop.cloud.common.jpa.models.AuditModel;
import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.SoftDelete;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SoftDelete(columnName = BaseModel.DELETED)
@SQLRestriction(BaseModel.DELETED_CONDITION)
@Table(name = TokenBlacklist.TABLE_NAME)
public class TokenBlacklist extends AuditModel {
    public static final String TABLE_NAME = "token_blacklist";

    public static final String TOKEN = "token";
    public static final String EXPIRED_AT = "expired_at";

    @Column(name = TOKEN)
    private String token;

    @Column(name = EXPIRED_AT)
    private OffsetDateTime expiredAt;
}
