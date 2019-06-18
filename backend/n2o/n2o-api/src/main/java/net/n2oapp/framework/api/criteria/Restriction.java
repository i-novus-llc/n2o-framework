package net.n2oapp.framework.api.criteria;

import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;

import java.util.Objects;

/**
 * Фильтр по полю
 */
public class Restriction extends Filter {

    private String fieldId;

    public Restriction(String fieldId, Filter filter) {
        super(filter);
        this.fieldId = fieldId;
    }

    public Restriction(String fieldId, Object value, FilterType type) {
        super(value, type);
        this.fieldId = fieldId;
    }

    public Restriction(String fieldId, Object value) {
        super(value);
        this.fieldId = fieldId;
    }

    public String getFieldId() {
        return fieldId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Restriction that = (Restriction) o;
        return Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fieldId);
    }
}
