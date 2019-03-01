package net.n2oapp.framework.access.metadata.accesspoint.model;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.Arrays;
import java.util.Objects;

/**
 * Точка доступа к объекту
 */
public class N2oObjectAccessPoint extends AccessPoint {

    private String objectId;
    private String action;
    private String[] removeFilters;

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String[] getRemoveFilters() {
        return removeFilters;
    }

    public void setRemoveFilters(String[] removeFilters) {
        this.removeFilters = removeFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        N2oObjectAccessPoint that = (N2oObjectAccessPoint) o;
        return Objects.equals(objectId, that.objectId) &&
                Objects.equals(action, that.action) &&
                Arrays.equals(removeFilters, that.removeFilters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), objectId, action);
        result = 31 * result + Arrays.hashCode(removeFilters);
        return result;
    }
}
