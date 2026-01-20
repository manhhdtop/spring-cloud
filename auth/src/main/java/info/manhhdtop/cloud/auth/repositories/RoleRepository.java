package info.manhhdtop.cloud.auth.repositories;

import info.manhhdtop.cloud.auth.models.Role;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface RoleRepository extends BaseRepository<@NonNull Role> {
    Optional<Role> findByName(String name);
}

