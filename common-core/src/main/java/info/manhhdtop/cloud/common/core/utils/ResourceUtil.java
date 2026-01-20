package info.manhhdtop.cloud.common.core.utils;

import java.io.InputStream;

public final class ResourceUtil {
    public static String loadResource(String path) {
        try (InputStream is = ResourceUtil.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("File not found in resources: " + path);
            }
            return new String(is.readAllBytes());
        } catch (Exception e) {
            throw new IllegalArgumentException("File not found in resources: " + path);
        }
    }
}
