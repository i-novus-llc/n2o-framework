package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Утилита для поиска фильтра
 */
public class ControlFilterUtil {

    /**
     * Находит подходящие фильтры для контрола.
     * Подходящими для контрола a являются a, a.b, a.c, a*.b, a*.c
     * Подходящими для контрола a.b являются a.b, a.b.c, a.b*.c
     *
     * @param controlId     идентификатор контрола
     * @param query         скомпилированный запрос
     * @return      подходящие фильтры
     */
    public static List<N2oQuery.Filter> getFilters(String controlId, CompiledQuery query) {
        List<N2oQuery.Filter> filters = new ArrayList<>();
        query.getFilterFieldsMap().keySet().forEach(filterId -> {
            if (controlId.equals(filterId)) {
                filters.add(query.getFilterFieldsMap().get(filterId));
            } else if (filterId.startsWith(controlId)) {
                String tmp = filterId.substring(controlId.length());
                if ((tmp.startsWith(".") && tmp.indexOf(".", 1) == -1) || (tmp.startsWith("*.") && tmp.indexOf(".", 2) == -1)) {
                    filters.add(query.getFilterFieldsMap().get(filterId));
                }
            }
        });
        return filters;
    }
}
