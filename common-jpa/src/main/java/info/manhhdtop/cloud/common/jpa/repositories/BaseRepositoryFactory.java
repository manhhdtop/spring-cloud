package info.manhhdtop.cloud.common.jpa.repositories;

import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

@Getter
public class BaseRepositoryFactory<T extends BaseModel> extends JpaRepositoryFactory {
    private final EntityManager entityManager;

    public BaseRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    @NonNull
    protected Class<?> getRepositoryBaseClass(@NonNull RepositoryMetadata metadata) {
        return BaseRepositoryImpl.class;
    }
}
