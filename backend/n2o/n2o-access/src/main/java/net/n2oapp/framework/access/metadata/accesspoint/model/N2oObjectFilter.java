package net.n2oapp.framework.access.metadata.accesspoint.model;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.util.Objects;

/**
 * Фильтр для object-filters в схеме прав доступа
 */
@Getter
@Setter
public class N2oObjectFilter extends N2oPreFilter {

    private String id;

    public N2oObjectFilter() {
    }

    public N2oObjectFilter(String fieldId, FilterType type, String id) {
        super(fieldId, type);
        this.id = id;
    }

    public N2oObjectFilter(String fieldId, String value, FilterType type, String id) {
        super(fieldId, value, type);
        this.id = id;
    }

    public N2oObjectFilter(String fieldId, String[] values, FilterType type, String id) {
        super(fieldId, values, type);
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        N2oObjectFilter that = (N2oObjectFilter) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
