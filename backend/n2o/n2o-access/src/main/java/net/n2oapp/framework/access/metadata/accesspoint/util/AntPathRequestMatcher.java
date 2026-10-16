package net.n2oapp.framework.access.metadata.accesspoint.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

/**
 * Сопоставление URL с шаблоном AntPath.
 */
public class AntPathRequestMatcher {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private final String pattern;

    public AntPathRequestMatcher(String pattern) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");
        this.pattern = ensureLeadingSlash(pattern);
    }

    public boolean matches(String url) {
        return MATCHER.match(pattern, ensureLeadingSlash(url));
    }

    private static String ensureLeadingSlash(String path) {
        return path.startsWith("/") ? path : "/" + path;
    }
}