package info.manhhdtop.cloud.auth.models;

import info.manhhdtop.cloud.common.jpa.models.AuditModel;
import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.*;
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
@Table(name = RefreshToken.TABLE_NAME)
public class RefreshToken extends AuditModel {
    public static final String TABLE_NAME = "refresh_token";

    public static final String USER_ID = "user_id";
    public static final String DEVICE_ID = "device_id";
    public static final String IP = "ip";
    public static final String USER_AGENT = "user_agent";
    public static final String TOKEN = "token";
    public static final String EXPIRED_AT = "expired_at";

    @ManyToOne
    @JoinColumn(name = USER_ID)
    private User user;

    @Column(name = DEVICE_ID)
    private String deviceId;

    @Column(name = IP)
    private String ip;

    @Column(name = USER_AGENT)
    private String userAgent;

    @Column(name = TOKEN)
    private String token;

    @Column(name = EXPIRED_AT)
    private OffsetDateTime expiredAt;
}
