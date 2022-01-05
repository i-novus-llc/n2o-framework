package net.n2oapp.framework.config.metadata.compile.widget;


import lombok.Getter;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Информация по фильтрам
 */
@Getter
@Deprecated
public class FiltersScope {
    /**
     * Фильтры собираемые во время компиляции
     */
    private Map<String, List<Filter>> datasourceFilters = new StrictMap<>();

    public FiltersScope() {
    }

    public List<Filter> getFilters(String sourceDatasource) {
        if (!datasourceFilters.containsKey(sourceDatasource))
            datasourceFilters.put(sourceDatasource, new ArrayList<>());
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
