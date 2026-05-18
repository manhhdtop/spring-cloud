package info.manhhdtop.cloud.common.core.i18n;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import java.util.List;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public final class LocaleSupport {
    public static final Locale DEFAULT_LOCALE = Locale.forLanguageTag("vi");
    private static final List<String> SUPPORTED_TAGS = List.of("vi", "en");

    private LocaleSupport() {
    }

    public static Locale resolve(String localeValue) {
        if (!StringUtils.hasText(localeValue)) {
            return DEFAULT_LOCALE;
        }
        String tag = localeValue.strip().toLowerCase(Locale.ROOT);
        if (tag.startsWith("en")) {
            return Locale.ENGLISH;
        }
        if (tag.startsWith("vi")) {
            return Locale.forLanguageTag("vi");
        }
        return DEFAULT_LOCALE;
    }

    public static Locale resolveFromAcceptLanguage(String acceptLanguage) {
        if (!StringUtils.hasText(acceptLanguage)) {
            return DEFAULT_LOCALE;
        }
        List<Locale.LanguageRange> ranges = Locale.LanguageRange.parse(acceptLanguage);
        String match = Locale.lookupTag(ranges, SUPPORTED_TAGS);
        if (match != null) {
            return resolve(match);
        }
        return DEFAULT_LOCALE;
    }

    public static Locale currentLocale() {
        if (LocaleContextHolder.getLocaleContext() != null) {
            return LocaleContextHolder.getLocale();
        }
        return DEFAULT_LOCALE;
    }
}
