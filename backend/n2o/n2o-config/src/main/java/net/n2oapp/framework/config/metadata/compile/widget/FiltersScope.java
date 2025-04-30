package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.meta.Filter;

import java.util.*;

/**
 * Информация по фильтрам
 */
@Getter
public class FiltersScope {
    /**
     * Фильтры собираемые во время компиляции
     */
    private Map<String, List<Filter>> datasourceFilters = new HashMap<>();

    public FiltersScope() {
    }

    public List<Filter> getFilters(String sourceDatasource) {
        datasourceFilters.computeIfAbsent(sourceDatasource, k -> new ArrayList<>());
        return datasourceFilters.get(sourceDatasource);
    }

    /**
     * Добавить фильтр
     *
     * @param filter Фильтр
     */
    public void addFilter(String sourceDatasource, Filter filter) {
        List<Filter> filters = datasourceFilters.compute(sourceDatasource, (k, v) -> v == null ? new ArrayList<>() : v);
        Optional<Filter> sameFilter = filters.stream()
                .filter(f -> f.getFilterId().equals(filter.getFilterId()) && f.getLink().equalsLink(filter.getLink()))
                .findAny();
        if (sameFilter.isEmpty())
            filters.add(filter);
    }

}
