package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Знание о префильтрах метаданной
 */
public interface PreFiltersAware {
    N2oPreFilter[] getPreFilters();

    void setPreFilters(N2oPreFilter[] preFilters);
}
