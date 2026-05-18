package info.manhhdtop.cloud.auth.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("application.jwt")
@Getter
@Setter
public class JwtProperty {
    private String privateKey;
    private String publicKey;
    private long accessTokenExpiryTime;
    private long refreshTokenExpiryTime;
    /** When true, {@link #privateKey} and {@link #publicKey} are classpath resource paths. */
    private boolean fromFile;
}
