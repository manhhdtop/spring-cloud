package info.manhhdtop.cloud.common.jpa.repositories;

import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import jakarta.persistence.EntityManager;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

@SuppressWarnings("unused")
public class BaseRepositoryFactoryBean<JR extends JpaRepository<@NonNull T, @NonNull Long>, T extends BaseModel, Long>
        extends JpaRepositoryFactoryBean<@NonNull JR, @NonNull T, @NonNull Long> {
    public BaseRepositoryFactoryBean(Class<? extends JR> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    @NonNull
    protected RepositoryFactorySupport createRepositoryFactory(@NonNull EntityManager entityManager) {
        return new BaseRepositoryFactory<T>(entityManager);
    }
}
