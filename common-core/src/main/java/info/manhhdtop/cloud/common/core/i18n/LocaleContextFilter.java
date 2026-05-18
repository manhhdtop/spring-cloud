package info.manhhdtop.cloud.common.core.i18n;

import info.manhhdtop.cloud.common.core.constants.HeaderConstant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import org.jspecify.annotations.NonNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class LocaleContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        Locale locale = resolveLocale(request);
        LocaleContextHolder.setLocaleContext(new SimpleLocaleContext(locale), true);
        try {
            filterChain.doFilter(request, response);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    static Locale resolveLocale(HttpServletRequest request) {
        String explicit = request.getHeader(HeaderConstant.X_LOCALE);
        if (StringUtils.hasText(explicit)) {
            return LocaleSupport.resolve(explicit);
        }
        String lang = request.getParameter("lang");
        if (StringUtils.hasText(lang)) {
            return LocaleSupport.resolve(lang);
        }
        return LocaleSupport.resolveFromAcceptLanguage(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
    }
}
