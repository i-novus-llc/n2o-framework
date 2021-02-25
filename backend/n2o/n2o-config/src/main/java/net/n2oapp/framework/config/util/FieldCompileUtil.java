package net.n2oapp.framework.config.util;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Утилита для компиляции поля
 */
public class FieldCompileUtil {

    public static N2oField.Dependency[] getResetOnChangeDependency(PreFiltersAware source) {
        List<N2oField.Dependency> dependencies = new ArrayList<>();

        if (source.getPreFilters() != null) {
            for (N2oPreFilter preFilter : source.getPreFilters()) {
                if (Boolean.TRUE.equals(preFilter.getResetOnChange())
                        && StringUtils.isLink(preFilter.getValue())) {
                    N2oField.ResetDependency reset = new N2oField.ResetDependency();
                    reset.setOn(new String[]{preFilter.getValue().substring(1, preFilter.getValue().length() - 1)});
                    dependencies.add(reset);
                }
            }
        }
        return dependencies.toArray(N2oField.Dependency[]::new);
    }
}
