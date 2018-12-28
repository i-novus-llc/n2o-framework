package net.n2oapp.framework.access.metadata.accesspoint.model;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author V. Alexeev.
 */
public class N2oColumnAccessPoint extends AccessPoint {

    private String pageId;
    private String containerId;
    private String columnId;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        N2oColumnAccessPoint that = (N2oColumnAccessPoint) o;

        if (!pageId.equals(that.pageId)) return false;
        if (!containerId.equals(that.containerId)) return false;
        return columnId.equals(that.columnId);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + pageId.hashCode();
        result = 31 * result + containerId.hashCode();
        result = 31 * result + columnId.hashCode();
        return result;
    }
}
