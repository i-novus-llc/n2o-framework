package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Информация для получения метаданной по url
 */
public class RouteInfoKey implements Comparable<RouteInfoKey> {

    /**
     * Шаблон URL в Ant стиле {@link org.springframework.util.AntPathMatcher}, используемый для поиска подходящего контекста расположения метаданной.
     */
    private String urlMatching;

    /**
     * Класс собранной метаданной
     */
    private Class<? extends Compiled> compiledClass;

    public RouteInfoKey(String urlPattern, Class<? extends Compiled> compiledClass) {
        this.compiledClass = compiledClass;
        setUrlMatching(urlPattern);
    }

    public Class<? extends Compiled> getCompiledClass() {
        return compiledClass;
    }

    public void setCompiledClass(Class<? extends Compiled> compiledClass) {
        this.compiledClass = compiledClass;
    }

    public void setUrlMatching(String urlPattern) {
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

    @Override
    public int compareTo(RouteInfoKey routeInfo) {
        String[] splitUrl = urlMatching.split("/");
        String[] splitUrl2 = routeInfo.urlMatching.split("/");
        int res = 0;
        for (int i = 0; i < splitUrl.length && i < splitUrl2.length; i++) {
            res = splitUrl[i].compareTo(splitUrl2[i]);
            if (res != 0)
                return res > 0 ? -1 : 1;
        }
        if (res == 0 && splitUrl.length != splitUrl2.length) {
            return splitUrl.length < splitUrl2.length ? 1 : -1;
        }
        if (this.compiledClass.equals(routeInfo.getCompiledClass())) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteInfoKey that = (RouteInfoKey) o;

        if (!urlMatching.equals(that.urlMatching)) return false;
        return this.getCompiledClass().equals(that.getCompiledClass());
    }

    @Override
    public int hashCode() {
        int result = urlMatching.hashCode();
        result = 31 * result + this.getCompiledClass().hashCode();
        return result;
    }
}
