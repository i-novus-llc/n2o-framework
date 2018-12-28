package net.n2oapp.framework.access.metadata.accesspoint.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;


/**
 * копипаст, с такого же класса в спринге
 */
public class AntPathRequestMatcher {

    private static final String MATCH_ALL = "/**";
    private final Matcher matcher;
    private final String pattern;


    public AntPathRequestMatcher(String pattern) {
        Assert.hasText(pattern, "Pattern cannot be null or empty");

        if (pattern.equals(MATCH_ALL) || pattern.equals("**")) {
            pattern = MATCH_ALL;
            matcher = null;
        } else {
            pattern = ignoreStartSeparater(pattern);

            // If the pattern ends with {@code /**} and has no other wildcards, then optimize to a sub-path match
            if (pattern.endsWith(MATCH_ALL) && pattern.indexOf('?') == -1 &&
                    pattern.indexOf("*") == pattern.length() - 2) {
                matcher = new SubpathMatcher(pattern.substring(0, pattern.length() - 3));
            } else {
                matcher = new SpringAntMatcher(pattern);
            }
        }

        this.pattern = pattern;
    }


    public boolean matches(String url) {
        return pattern.equals(MATCH_ALL) || matcher.matches(ignoreStartSeparater(url));
    }

    private String ignoreStartSeparater(String url) {
        return url.startsWith("/") ? url : "/" + url;
    }


    private static interface Matcher {
        boolean matches(String path);
    }

    private static class SpringAntMatcher implements Matcher {
        private static final AntPathMatcher antMatcher = new AntPathMatcher();

        private final String pattern;

        private SpringAntMatcher(String pattern) {
            this.pattern = pattern;
        }

        public boolean matches(String path) {
            return antMatcher.match(pattern, path);
        }
    }

    /**
     * Optimized matcher for trailing wildcards
     */
    private static class SubpathMatcher implements Matcher {
        private final String subpath;
        private final int length;

        private SubpathMatcher(String subpath) {
            assert !subpath.contains("*");
            this.subpath = subpath;
            this.length = subpath.length();
        }

        public boolean matches(String path) {
            return path.startsWith(subpath) && (path.length() == length || path.charAt(length) == '/');
        }
    }
}

