package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

/**
 * Информация для получения метаданной по url
 */
public class RouteInfo implements Comparable<RouteInfo> {
    /**
     * Шаблон URL, используемый для получения параметров запроса.
     */
    private String urlPattern;

    /**
     * Шаблон URL в Ant стиле {@link org.springframework.util.AntPathMatcher}, используемый для поиска подходящего контекста расположения метаданной.
     */
    private String urlMatching;

    /**
     * Контекст сборки метаданной.
     */
    private CompileContext<? extends Compiled, ?> context;

    public RouteInfo(String urlPattern, CompileContext<? extends Compiled, ?> context) {
        setContext(context);
        setUrlPattern(urlPattern);
    }

    public CompileContext<? extends Compiled, ?> getContext() {
        return context;
    }

    public void setContext(CompileContext<? extends Compiled, ?> context) {
        this.context = context;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
        StringBuilder urlMatching;
        String[] splited = urlPattern.split("/");
        if (splited.length > 0) {
            urlMatching = new StringBuilder(splited[0]);
            for (int i = 1; i < splited.length; i++) {
                if (splited[i].startsWith(":")) {
                    urlMatching.append("/").append("*");
                } else {
                    urlMatching.append("/").append(splited[i]);
                }
            }
        } else {
            urlMatching = new StringBuilder(urlPattern);
        }
        this.urlMatching = urlMatching.toString();
    }

    public String getUrlMatching() {
        return urlMatching;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    @Override
    public int compareTo(RouteInfo routeInfo) {
        String[] splitUrl = urlMatching.split("/");
        String[] splitUrl2 = routeInfo.urlMatching.split("/");
        int res = 0;
        for (int i = 0; i < splitUrl.length && i < splitUrl2.length; i++) {
            res = splitUrl[i].compareTo(splitUrl2[i]);
            if (res == 0) {
                continue;
            } else {
                return res > 0 ? -1 : 1;
            }
        }
        if (res == 0 && splitUrl.length != splitUrl2.length) {
            return splitUrl.length < splitUrl2.length ? 1 : -1;
        }
        if (context.getCompiledClass().equals(routeInfo.getContext().getCompiledClass())) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteInfo info = (RouteInfo) o;

        if (!urlMatching.equals(info.urlMatching)) return false;
        return context.getCompiledClass().equals(info.context.getCompiledClass());
    }

    @Override
    public int hashCode() {
        int result = urlMatching.hashCode();
        result = 31 * result + context.getCompiledClass().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return  getUrlPattern() + " -> " + getContext();
    }
}
