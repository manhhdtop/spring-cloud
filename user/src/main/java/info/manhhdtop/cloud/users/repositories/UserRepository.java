package info.manhhdtop.cloud.users.repositories;

import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import info.manhhdtop.cloud.users.models.User;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public interface UserRepository extends BaseRepository<@NonNull User> {
    Optional<User> findByEmail(String email);
}
