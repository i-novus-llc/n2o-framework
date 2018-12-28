package net.n2oapp.framework.access.metadata.accesspoint.model;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.CompilerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author V. Alexeev.
 */
public class N2oFilterAccessPoint extends AccessPoint {

    private String queryId;
    private String filterId;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        N2oFilterAccessPoint point = (N2oFilterAccessPoint) o;

        if (!queryId.equals(point.queryId)) return false;
        return filterId.equals(point.filterId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + queryId.hashCode();
        result = 31 * result + filterId.hashCode();
        return result;
    }
}
