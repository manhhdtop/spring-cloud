package info.manhhdtop.cloud.auth.configs;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtPropertyHolder {
    @Getter
    private static String privateKey;
    @Getter
    private static String publicKey;
    @Getter
    private static long accessTokenExpiryTime;
    @Getter
    private static long refreshTokenExpiryTime;
    @Getter
    private static boolean fromFile;

    public JwtPropertyHolder(JwtProperty jwtProperty) {
        JwtPropertyHolder.privateKey = jwtProperty.getPrivateKey();
        JwtPropertyHolder.publicKey = jwtProperty.getPublicKey();
        JwtPropertyHolder.accessTokenExpiryTime = jwtProperty.getAccessTokenExpiryTime();
        JwtPropertyHolder.refreshTokenExpiryTime = jwtProperty.getRefreshTokenExpiryTime();
        JwtPropertyHolder.fromFile = jwtProperty.isFromFile();
    }
}
