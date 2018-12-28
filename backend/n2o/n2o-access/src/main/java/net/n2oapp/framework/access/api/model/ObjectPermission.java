package net.n2oapp.framework.access.api.model;

import net.n2oapp.framework.access.api.model.filter.N2oAccessFilter;

import java.util.Collections;
import java.util.List;

/**
 * User: operhod
 * Date: 28.10.13
 * Time: 15:58
 */
public class ObjectPermission extends Permission {

    private String objectId;

    public static ObjectPermission denied(String detailedMessage, String techMessage) {
        ObjectPermission res = new ObjectPermission(false);
        res.setDetailedMessage(detailedMessage);
        res.setTechMessage(techMessage);
        return res;
    }

    public static ObjectPermission allowed(String techMessage) {
        ObjectPermission res = new ObjectPermission(true);
        res.setTechMessage(techMessage);
        return res;
    }


    private List<N2oAccessFilter> accessFilters;

    private AccessDeniedScope accessDeniedScope = AccessDeniedScope.object;

    public AccessDeniedScope getAccessDeniedScope() {
        return accessDeniedScope;
    }

    public void setAccessDeniedScope(AccessDeniedScope accessDeniedScope) {
        this.accessDeniedScope = accessDeniedScope;
    }

    public ObjectPermission(boolean allowed) {
        super(allowed);
    }

    public ObjectPermission(boolean allowed, String objectId) {
        super(allowed);
        this.objectId = objectId;
    }

    public List<N2oAccessFilter> getAccessFilters() {
        if (accessFilters == null)
            return Collections.emptyList();
        return accessFilters;
    }

    public void setAccessFilters(List<N2oAccessFilter> accessFilters) {
        this.accessFilters = accessFilters;
    }

    public String getObjectId() {
        return objectId;
    }
}
