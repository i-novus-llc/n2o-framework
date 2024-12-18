package net.n2oapp.framework.api.metadata.meta.page;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ReduxAction;

import java.util.*;

/**
 * Модель маршрутов страницы
 */
@Setter
@Getter
@NoArgsConstructor
public class PageRoutes implements Compiled {
    private Set<String> set = new HashSet<>();
    @JsonProperty
    private Map<String, Query> queryMapping = new LinkedHashMap<>();
    @JsonProperty
    private List<String> subRoutes;
    @JsonProperty
    private String path;

    public PageRoutes(String route) {
        set.add(route);
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
            throw new N2oException(String.format("Page already contains query mapping %s!", queryParam));
        }
        queryMapping.put(queryParam, query);
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
