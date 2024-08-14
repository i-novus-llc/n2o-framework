package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.StringUtils.isLink;
import static net.n2oapp.framework.api.StringUtils.unwrapLink;

/**
 * Утилита для компиляции поля
 */
public class FieldCompileUtil {

    /**
     * Инициализация reset зависимостей по префильтрам метаданных
     *
     * @param source Метаданная, содержащая префильтры
     * @return Массив зависимостей
     */
    public static N2oField.Dependency[] getResetOnChangeDependency(PreFiltersAware source) {
        List<N2oField.Dependency> dependencies = new ArrayList<>();

        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                if (Boolean.TRUE.equals(preFilter.getResetOnChange()) && isLink(preFilter.getValue())) {
                    N2oField.ResetDependency reset = new N2oField.ResetDependency();
                    reset.setOn(new String[]{unwrapLink(preFilter.getValue())});
                    reset.setApplyOnInit(false);
                    dependencies.add(reset);
                }
            }
        }
        return dependencies.toArray(N2oField.Dependency[]::new);
    }

    /**
     * Находит подходящие фильтры для поля.
     * Подходящими для поля a являются a, a.b, a.c, a*.b, a*.c
     * Подходящими для поля a.b являются a.b, a.b.c, a.b*.c
     *
     * @param fieldId Идентификатор поля
     * @param query   Скомпилированная модель выборки
     * @return Список подходящих для поля фильтров
     */
    public static List<N2oQuery.Filter> getFilters(String fieldId, CompiledQuery query) {
        List<N2oQuery.Filter> filters = new ArrayList<>();
        if (fieldId == null)
            return filters;
        query.getFilterFieldsMap().keySet().forEach(filterId -> {
            if (fieldId.equals(filterId)) {
                filters.add(query.getFilterFieldsMap().get(filterId));
            } else if (filterId.startsWith(fieldId)) {
                String tmp = filterId.substring(fieldId.length());
                if ((tmp.startsWith(".") && tmp.indexOf(".", 1) == -1) ||
                        (tmp.startsWith("*.") && tmp.indexOf(".", 2) == -1)) {
                    filters.add(query.getFilterFieldsMap().get(filterId));
                }
            }
        });
        return filters;
    }
}
