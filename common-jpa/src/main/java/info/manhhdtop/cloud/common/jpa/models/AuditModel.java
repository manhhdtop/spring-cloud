package info.manhhdtop.cloud.common.jpa.models;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuditModel extends BaseModel {
    private static final String CREATED_BY = "created_by";
    private static final String UPDATED_BY = "updated_by";

    @Column(name = CREATED_BY)
    @CreatedBy
    private Long createdBy;
    @Column(name = UPDATED_BY)
    @LastModifiedBy
    private Long updatedBy;
}
