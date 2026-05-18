package info.manhhdtop.cloud.common.core.utils;

import info.manhhdtop.cloud.common.core.i18n.LocaleSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractMessageUtil {
    protected static String BASE_NAME;

    protected AbstractMessageUtil(String baseName) {
        BASE_NAME = baseName;
    }

    public static String get(String key) {
        return get(key, (Object[]) null);
    }

    public static String get(String key, Object... args) {
        Locale locale = LocaleContextHolder.getLocaleContext() != null
                ? LocaleContextHolder.getLocale()
                : LocaleSupport.DEFAULT_LOCALE;
        return get(key, args, locale);
    }

    public static String get(String key, Object[] args, Locale locale) {
        if (!StringUtils.isNotBlank(key)) {
            return key;
        }
        if (BASE_NAME == null) {
            MessageUtil.init();
        }
        Locale effectiveLocale = locale != null ? locale : LocaleSupport.DEFAULT_LOCALE;
        try {
            ResourceBundle resourceBundle = loadBundle(BASE_NAME, effectiveLocale);
            String message = resourceBundle.getString(key);
            if (args == null || args.length == 0) {
                return message;
            }
            return MessageFormat.format(message, args);
        } catch (MissingResourceException ex) {
            log.debug("Missing message key: {}", key, ex);
            return key;
        } catch (Exception ex) {
            log.debug("Failed to resolve message key: {}", key, ex);
            return key;
        }
    }

    private static ResourceBundle loadBundle(String baseName, Locale locale) {
        try {
            return ResourceBundle.getBundle(baseName, locale, Utf8Control.INSTANCE);
        } catch (MissingResourceException ex) {
            return ResourceBundle.getBundle(baseName, Locale.ROOT, Utf8Control.INSTANCE);
        }
    }

    private static final class Utf8Control extends ResourceBundle.Control {
        private static final Utf8Control INSTANCE = new Utf8Control();

        @Override
        public ResourceBundle newBundle(
                String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream == null) {
                    return null;
                }
                try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    return new PropertyResourceBundle(reader);
                }
            }
        }
    }
}
