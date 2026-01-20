package info.manhhdtop.cloud.auth.repositories;

import info.manhhdtop.cloud.auth.models.TokenBlacklist;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import org.jspecify.annotations.NonNull;

public interface TokenBlacklistRepository extends BaseRepository<@NonNull TokenBlacklist> {
}
