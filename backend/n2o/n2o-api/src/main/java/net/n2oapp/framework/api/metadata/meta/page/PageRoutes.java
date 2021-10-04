package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Модель маршрутов страницы
 */
@Setter
@Getter
public class PageRoutes implements Compiled {
    @JsonProperty
    private List<Route> list = new ArrayList<>();
    @JsonProperty
    private Map<String, ReduxAction> pathMapping = new LinkedHashMap<>();
    @JsonProperty
    private Map<String, Query> queryMapping = new LinkedHashMap<>();

    /**
     * Найти маршрут страницы по шаблону URL
     *
     * @param urlPattern Шаблон URL
     * @return Маршрут страницы
     */
    public Route findRouteByUrl(String urlPattern) {
        return list.stream().filter(r -> r.getPath().equals(urlPattern)).findFirst()
                .orElseThrow(() -> new N2oException("Route by url [" + urlPattern + "] not found"));
    }

    /**
     * Добавить маршрут к странице
     *
     * @param route Путь
     */
    public void addRoute(Route route) {
        if (!this.list.contains(route) || route.isOtherPage) {
            if (route.getIsOtherPage() != null && route.getIsOtherPage()) {
                this.list.add(0, route);
            } else {
                this.list.add(route);
            }
        }
    }

    /**
     * Добавить маршрут к виджету страницы
     *
     * @param path Путь
     */
    public Route addRoute(String path) {
        Route route = new Route();
        route.setPath(path);
        addRoute(route);
        return route;
    }

    /**
     * Добавить параметр пути
     *
     * @param pathParam Параметр в пути
     * @param action    Redux действие при извлечении параметра в пути
     */
    public void addPathMapping(String pathParam, ReduxAction action) {
        if (pathMapping.containsKey(pathParam) && !pathMapping.get(pathParam).equals(action)) {
            throw new N2oException(String.format("Page already contains path mapping %s!", pathParam)).addData(pathParam);
        }
        pathMapping.put(pathParam, action);
    }

    /**
     * Добавить параметры путей
     *
     * @param pathMappings Параметры в пути
     */
    public void addPathMappings(Map<String, ReduxAction> pathMappings) {
        if (pathMappings != null)
            pathMappings.forEach((k, v) -> addPathMapping(k, v));
    }

    /**
     * Добавить параметр запроса
     *
     * @param queryParam Параметр в запросе
     * @param onGet      Redux действие при извлечении параметра запроса
     * @param onSet      Ссылка на Redux модель, при вставке параметра запроса
     */
    public void addQueryMapping(String queryParam, ReduxAction onGet, BindLink onSet) {
        Query query = new Query();
        query.setOnGet(onGet);
        query.setOnSet(onSet);
        if (queryMapping.containsKey(queryParam) && !queryMapping.get(queryParam).equals(query)) {
            throw new N2oException(String.format("Page already contains query mapping %s!", queryParam)).addData(queryParam);
        }
        queryMapping.put(queryParam, query);
    }

    /**
     * Модель маршрута
     */
    @Setter
    @Getter
    @NoArgsConstructor
    public static class Route implements Compiled {
        @JsonProperty
        private String path;
        @JsonProperty
        private Boolean exact = true;
        @JsonProperty
        private Boolean isOtherPage = false;
        /**
         * Признак, что маршрут виджета содержит выделенную запись
         */
        private boolean resolved;

        public Route(String path) {
            this.path = path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Route route = (Route) o;
            return path != null ? path.equals(route.path) : route.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * Маппинг параметров в маршруте
     */
    @Setter
    @Getter
    public static class Query implements Compiled {
        @JsonProperty("get")
        private ReduxAction onGet;
        @JsonProperty("set")
        private BindLink onSet;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Query query = (Query) o;

            if (onGet != null ? !onGet.equals(query.onGet) : query.onGet != null) return false;
            return onSet != null ? onSet.equals(query.onSet) : query.onSet == null;
        }

        @Override
        public int hashCode() {
            int result = onGet != null ? onGet.hashCode() : 0;
            result = 31 * result + (onSet != null ? onSet.hashCode() : 0);
            return result;
        }
    }
}
