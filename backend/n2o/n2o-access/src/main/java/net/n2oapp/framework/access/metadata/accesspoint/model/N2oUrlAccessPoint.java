package net.n2oapp.framework.access.metadata.accesspoint.model;


import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.util.AntPathRequestMatcher;


/**
 * Точка доступа к URL
 */

public class N2oUrlAccessPoint extends AccessPoint {

    private String pattern;
    private transient volatile AntPathRequestMatcher matcher;

    public AntPathRequestMatcher getMatcher() {
        if (matcher == null)
            initMatcher();
        return matcher;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    private synchronized void initMatcher() {
        if (matcher == null)
            matcher = new AntPathRequestMatcher(pattern);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        N2oUrlAccessPoint that = (N2oUrlAccessPoint) o;

        return pattern != null ? pattern.equals(that.pattern) : that.pattern == null;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }
}
