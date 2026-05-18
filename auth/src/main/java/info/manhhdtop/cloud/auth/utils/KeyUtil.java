package info.manhhdtop.cloud.auth.utils;

import info.manhhdtop.cloud.common.core.utils.ResourceUtil;
import io.jsonwebtoken.io.Decoders;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@SuppressWarnings("unused")
public final class KeyUtil {
    private static final String ALGORITHM = "RSA";
    private static final String REPLACE_REGEX = "-----.*-----";

    public static PrivateKey loadPrivateKey(String key) throws Exception {
        return loadPrivateKey(key, false);
    }

    public static PrivateKey loadPrivateKey(String key, boolean fromFile) throws Exception {
        byte[] keyBytes = fromFile ? loadKeyContent(key) : decodeKeyMaterial(key);
        return KeyUtil.loadPrivateKey(keyBytes);
    }

    public static PublicKey loadPublicKey(String key) throws Exception {
        return loadPublicKey(key, false);
    }

    public static PublicKey loadPublicKey(String key, boolean fromFile) throws Exception {
        byte[] keyBytes = fromFile ? loadKeyContent(key) : decodeKeyMaterial(key);
        return KeyUtil.loadPublicKey(keyBytes);
    }

    private static byte[] decodeKeyMaterial(String key) {
        String normalized = key.replaceAll(REPLACE_REGEX, "").replaceAll("\\s", "");
        return Decoders.BASE64.decode(normalized);
    }

    private static byte[] loadKeyContent(String filePath) {
        String pem = ResourceUtil.loadResource(filePath);
        return decodeKeyMaterial(pem);
    }

    public static PrivateKey loadPrivateKey(byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(spec);
    }

    public static PublicKey loadPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance(ALGORITHM).generatePublic(spec);
    }
}
