package info.manhhdtop.cloud.auth.repositories;

import info.manhhdtop.cloud.auth.models.Permission;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<@NonNull Permission> {
    Optional<Permission> findByName(String name);
}

