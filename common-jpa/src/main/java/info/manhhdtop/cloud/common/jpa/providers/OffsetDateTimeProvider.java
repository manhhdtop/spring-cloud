package info.manhhdtop.cloud.common.jpa.providers;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import org.springframework.data.auditing.DateTimeProvider;

public class OffsetDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(OffsetDateTime.now());
    }
}
