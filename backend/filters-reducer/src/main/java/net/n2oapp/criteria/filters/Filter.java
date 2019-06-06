package net.n2oapp.criteria.filters;

import java.io.Serializable;

/**
 * Фильтр
 */
public class Filter implements Serializable {

    private Object value;
    private final FilterType type;

    public Filter(Filter filter) {
        this.value = filter.getValue();
        this.type = filter.getType();
    }

    public Filter(Object value, FilterType type) {
        this.value = value;
        this.type = type;
    }

    public Filter(Object value) {
        this.value = value;
        this.type = FilterType.eq;
    }

    public Filter(FilterType type) {
        this.type = type;
        this.value = null;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public FilterType getType() {
        return type;
    }

    public boolean check(Object value) {
        return FilterChecker.check(this, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (type != filter.type) return false;
        if (value != null ? !value.equals(filter.value) : filter.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
