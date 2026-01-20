package info.manhhdtop.cloud.common.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractMessageUtil {
    protected static String BASE_NAME;

    protected AbstractMessageUtil(String baseName) {
        BASE_NAME = baseName;
    }

    public static String get(String key) {
        return get(key, null, LocaleContextHolder.getLocale());
    }

    public static String get(String key, Object[] args) {
        return get(key, args, LocaleContextHolder.getLocale());
    }

    public static String get(String key, Object[] args, Locale locale) {
        if (!StringUtils.isNotBlank(key)) {
            return key;
        }
        try {
            ResourceBundle resourceBundle;
            try {
                resourceBundle = ResourceBundle.getBundle(BASE_NAME, locale);
                if (!resourceBundle.getLocale().equals(locale)) {
                    resourceBundle = ResourceBundle.getBundle(BASE_NAME, Locale.ROOT);
                }
            } catch (MissingResourceException e) {
                resourceBundle = ResourceBundle.getBundle(BASE_NAME, Locale.ROOT);
            }

            String message;
            try {
                message = resourceBundle.getString(key);
                message = MessageFormat.format(message, args);
            } catch (Exception ex) {
                log.debug(ex.getMessage(), ex);
                message = key;
            }

            return message;
        } catch (Exception e) {
            return key;
        }
    }
}
