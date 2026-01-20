package info.manhhdtop.cloud.auth.repositories;

import info.manhhdtop.cloud.auth.models.User;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends BaseRepository<@NonNull User> {
    Optional<User> findByEmail(String email);
    
    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmailWithRolesAndPermissions(String email);
}
