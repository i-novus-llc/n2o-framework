package net.n2oapp.framework.access.metadata.accesspoint.model;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.Arrays;
import java.util.Objects;

/**
 * Список фильтров для объекта
 */

@Getter
@Setter
public class N2oObjectFiltersAccessPoint extends AccessPoint {

    private String objectId;
    private N2oObjectFilter[] filters;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        N2oObjectFiltersAccessPoint that = (N2oObjectFiltersAccessPoint) o;
        return Objects.equals(objectId, that.objectId) &&
                Arrays.equals(filters, that.filters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), objectId);
        result = 31 * result + Arrays.hashCode(filters);
        return result;
    }
}
