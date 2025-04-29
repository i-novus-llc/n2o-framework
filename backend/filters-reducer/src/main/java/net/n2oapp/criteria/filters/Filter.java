package net.n2oapp.criteria.filters;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Фильтр
 */
@Getter
@Setter
public class Filter implements Serializable {

    private Object value;
    private final FilterTypeEnum type;

    public Filter(Filter filter) {
        this.value = filter.getValue();
        this.type = filter.getType();
    }

    public Filter(Object value, FilterTypeEnum type) {
        this.value = value;
        this.type = type;
    }

    public Filter(Object value) {
        this.value = value;
        this.type = FilterTypeEnum.eq;
    }

    public Filter(FilterTypeEnum type) {
        this.type = type;
        this.value = null;
    }

    public boolean check(Object value) {
        return FilterChecker.check(this, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Filter filter = (Filter) o;

        if (type != filter.type)
            return false;
        return Objects.equals(value, filter.value);
    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
