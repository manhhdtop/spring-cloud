package info.manhhdtop.cloud.auth.repositories;

import info.manhhdtop.cloud.auth.models.RefreshToken;
import info.manhhdtop.cloud.common.jpa.repositories.BaseRepository;
import org.jspecify.annotations.NonNull;

public interface RefreshTokenRepository extends BaseRepository<@NonNull RefreshToken> {
}
