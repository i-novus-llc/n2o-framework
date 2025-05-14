package net.n2oapp.framework.config.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.global.dao.query.field.QuerySimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.register.route.RouteUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилита по работе с QueryContext
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryContextUtil {

    private static final String SORTING = "sorting.";

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
            e.setFilterId(f.getFilterId());
            e.setRoutable(true);
            return e;
        }).toList();
        contextForRegister.setFilters(filters);
        contextForRegister.setSortingMap(initSortingMap(query));
        return contextForRegister;
    }

    public static Map<String, String> initSortingMap(CompiledQuery query) {
        Map<String, String> sortingMap = new HashMap<>();
        for (QuerySimpleField sortingField : query.getSortingFields()) {
            sortingMap.put(
                    SORTING + RouteUtil.normalizeParam(sortingField.getId()),
                    sortingField.getId()
            );
        }

        return sortingMap;
    }
}
