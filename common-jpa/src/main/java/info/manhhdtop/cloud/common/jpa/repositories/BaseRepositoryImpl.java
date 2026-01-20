package info.manhhdtop.cloud.common.jpa.repositories;

import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@Getter
public class BaseRepositoryImpl<T extends BaseModel> extends SimpleJpaRepository<@NonNull T, @NonNull Long> implements BaseRepository<T> {
    protected final EntityManager entityManager;
    private final Class<T> entityClass;

    public BaseRepositoryImpl(JpaEntityInformation<@NonNull T, @NonNull String> entityInformation,
                              EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }
}
