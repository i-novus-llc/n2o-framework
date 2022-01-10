package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.api.metadata.meta.Filter;

/**
 * Информация о фильтрах таблицы
 */
public class TableFiltersScope {
    private String sourceDatasourceId;
    private FiltersScope filtersScope;

    public TableFiltersScope(String sourceDatasourceId, FiltersScope filtersScope) {
        this.sourceDatasourceId = sourceDatasourceId;
        this.filtersScope = filtersScope;
    }

    public void addFilter(Filter filter) {
        filtersScope.addFilter(sourceDatasourceId, filter);
    }
}
