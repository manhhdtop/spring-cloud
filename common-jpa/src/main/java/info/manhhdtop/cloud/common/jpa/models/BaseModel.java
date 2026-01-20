package info.manhhdtop.cloud.common.jpa.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseModel {
    public static final String ID = "id";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String VERSION = "version";
    public static final String DELETED = "is_deleted";
    public static final String DELETED_CONDITION = "is_deleted IS FALSE";

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = CREATED_AT)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = UPDATED_AT)
    private OffsetDateTime updatedAt;

    @Version
    private Long version;
}
