package info.manhhdtop.cloud.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
@SuppressWarnings("unused")
public class EncryptionUtil {
    private static final String SHA_256_ALGORITHM = "SHA-256";
    private static final String SHA_512_ALGORITHM = "SHA-512";
    private static final MessageDigest digestSha256;
    private static final MessageDigest digestSha512;

    static {
        try {
            digestSha256 = MessageDigest.getInstance(SHA_256_ALGORITHM);
            digestSha512 = MessageDigest.getInstance(SHA_512_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha256(String input) {
        return sha256(input, true);
    }

    public static String sha256(String input, boolean padding) {
        try {
            byte[] hash = digestSha256.digest(input.getBytes(StandardCharsets.UTF_8));
            String encoded;
            if (padding) {
                encoded = Base64.getUrlEncoder().encodeToString(hash);
            } else {
                encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            }
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sha512(String input) {
        return sha512(input, true);
    }

    public static String sha512(String input, boolean padding) {
        try {
            byte[] hash = digestSha512.digest(input.getBytes(StandardCharsets.UTF_8));
            String encoded;
            if (padding) {
                encoded = Base64.getUrlEncoder().encodeToString(hash);
            } else {
                encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
            }
            return encoded;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
