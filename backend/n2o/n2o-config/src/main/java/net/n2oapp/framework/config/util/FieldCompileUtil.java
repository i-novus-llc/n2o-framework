package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

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
                    dependencies.add(reset);
                }
            }
        }
        return dependencies.toArray(N2oField.Dependency[]::new);
    }
}
