package info.manhhdtop.cloud.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@SuppressWarnings("all")
public final class StringUtils {
    public static final int DEFAULT_STRING_LENGTH = 6;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String CHARACTERS_WITH_SPECIAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-=_+";
    private static final SecureRandom RANDOM = new SecureRandom();

    private StringUtils() {
    }

    public static boolean isBlank(String str) {
        return (str == null || str.isEmpty() || !containsText(str));
    }

    public static boolean isNotBlank(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && !str.isEmpty());
    }

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String strip(String str) {
        if (str == null) {
            return null;
        }
        return str.strip();
    }

    public static String stripNonNull(String str) {
        if (str == null) {
            return "";
        }
        return str.strip();
    }

    public static boolean startsWith(String token, String str) {
        if (isBlank(token) || !hasLength(str)) {
            return false;
        }
        return str.startsWith(token);
    }

    public static boolean endsWith(String token, String str) {
        if (isBlank(token) || !hasLength(str)) {
            return false;
        }
        return str.endsWith(token);
    }

    public static String base64Encode(String str) {
        if (isBlank(str)) {
            return "";
        }
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64Encode(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String base64Decode(String str) {
        if (isBlank(str)) {
            return "";
        }
        try {
            return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("(base64Decode) string: {} | exception: {}", str, e.getMessage());
            return str;
        }
    }

    public static boolean equals(String s1, String s2) {
        return equals(s1, s2, false);
    }

    public static boolean equals(String s1, String s2, boolean striped) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        if (striped) {
            return s1.strip().equals(s2.strip());
        }
        return s1.equals(s2);
    }

    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1 == null || s2 == null) {
            return false;
        }
        return s1.equalsIgnoreCase(s2);
    }

    public static Long length(String content) {
        return content == null ? 0L : (long) content.length();
    }

    public static String randomString() {
        return randomString(DEFAULT_STRING_LENGTH);
    }

    public static String randomString(boolean withSpecialChars) {
        return randomString(DEFAULT_STRING_LENGTH, withSpecialChars);
    }

    public static String randomString(int length) {
        return randomString(length, false);
    }

    public static String randomString(int length, boolean withSpecialChars) {
        var chars = withSpecialChars ? CHARACTERS_WITH_SPECIAL_CHARS : CHARACTERS;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
