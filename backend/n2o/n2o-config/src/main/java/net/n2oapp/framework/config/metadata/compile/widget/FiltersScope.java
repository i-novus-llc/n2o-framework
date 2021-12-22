package net.n2oapp.framework.config.metadata.compile.widget;


import lombok.AllArgsConstructor;
import lombok.Getter;
import net.n2oapp.framework.api.metadata.meta.Filter;

import java.util.List;
import java.util.Optional;

/**
 * Информация по фильтрам
 */
@Getter
@AllArgsConstructor
@Deprecated
public class FiltersScope {
    /**
     * Фильтры собираемые во время компиляции
     */
    List<Filter> filters;

    /**
     * Добавить фильтр
     *
     * @param filter Фильтр
     */
    public void addFilter(Filter filter) {
        Optional<Filter> sameFilter = filters.stream()
                .filter(f -> f.getFilterId().equals(filter.getFilterId()) && f.getLink().equalsLink(filter.getLink()))
                .findAny();
        if (sameFilter.isEmpty())
            filters.add(filter);
    }

}
