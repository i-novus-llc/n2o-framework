package net.n2oapp.framework.api.criteria;

import lombok.Getter;
import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;

import java.util.Objects;

/**
 * Фильтр по полю
 */
@Getter
public class Restriction extends Filter {

    private String id;
    private final String fieldId;

    public Restriction(String fieldId, Filter filter) {
        super(filter);
        this.fieldId = fieldId;
    }

    public Restriction(String fieldId, Object value, FilterTypeEnum type) {
        super(value, type);
        this.fieldId = fieldId;
    }

    public Restriction(String id, String fieldId, Object value, FilterTypeEnum type) {
        super(value, type);
        this.fieldId = fieldId;
        this.id = id;
    }

    public Restriction(String fieldId, Object value) {
        super(value);
        this.fieldId = fieldId;
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
