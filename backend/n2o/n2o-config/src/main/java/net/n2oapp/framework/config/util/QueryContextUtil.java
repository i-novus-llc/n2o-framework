package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.List;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.DynamicUtil.isDynamic;

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
        String queryId = query.getId();
        QueryContext contextForRegister = new QueryContext(queryId, isDynamic(queryId) ?
                query.getRoute() + "?" + RouteUtil.parseQuery(queryId) : query.getRoute());
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
