package net.n2oapp.framework.access.metadata.accesspoint.model;


import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

import java.util.List;

public class N2oReferenceAccessPoint extends AccessPoint {

    private String objectId;


    public N2oReferenceAccessPoint() {
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        N2oReferenceAccessPoint that = (N2oReferenceAccessPoint) o;
        if (objectId != null ? !objectId.equals(that.objectId) : that.objectId != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (objectId != null ? objectId.hashCode() : 0);
        return result;
    }
}
