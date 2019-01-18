package net.n2oapp.framework.access.metadata.accesspoint.model;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Точка доступа к объекту
 */
public class N2oObjectAccessPoint extends AccessPoint {

    private String objectId;
    private String action;
    private N2oPreFilter[] accessFilters;

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

    public N2oPreFilter[] getAccessFilters() {
        return accessFilters;
    }

    public List<N2oPreFilter> getAccessFiltersAsList() {
        return Arrays.asList(accessFilters);
    }

    public void setAccessFilters(N2oPreFilter[] accessFilters) {
        this.accessFilters = accessFilters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        N2oObjectAccessPoint that = (N2oObjectAccessPoint) o;

        if (!objectId.equals(that.objectId)) return false;
        if (!Objects.equals(action, that.action)) return false;
        return Objects.equals(accessFilters, that.accessFilters);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + objectId.hashCode();
        result = 31 * result + (action != null ? action.hashCode() : 0);
        result = 31 * result + (accessFilters != null ? accessFilters.hashCode() : 0);
        return result;
    }


}
