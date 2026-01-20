package info.manhhdtop.cloud.common.jpa.repositories;

import info.manhhdtop.cloud.common.jpa.models.BaseModel;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModel> extends JpaRepository<@NonNull T, @NonNull Long> {
    long count(Specification<@NonNull T> specification);

    List<T> findAll(Specification<@NonNull T> specification);

    Page<@NonNull T> findAll(Specification<@NonNull T> specification, Pageable pageable);
}
