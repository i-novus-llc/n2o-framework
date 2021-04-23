package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилита по работе с QueryContext
 */
public class QueryContextUtil {

    /**
     * Создание QueryContext для RouteRegister
     *
     * @param query Исходная выборка
     * @return QueryContext с добавленными фильтрами
     */
    public static QueryContext prepareQueryContextForRouteRegister(CompiledQuery query) {
        QueryContext contextForRegister = new QueryContext(query.getId(), query.getRoute());
        List<Filter> filters = query.getFilterFieldsMap().values().stream().map(f -> {
            Filter e = new Filter();
            e.setParam(f.getParam());
            e.setFilterId(f.getFilterField());
            e.setRoutable(true);
            return e;
        }).collect(Collectors.toList());
        contextForRegister.setFilters(filters);
        return contextForRegister;
    }
}
