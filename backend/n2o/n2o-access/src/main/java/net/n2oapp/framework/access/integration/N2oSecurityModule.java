package net.n2oapp.framework.access.integration;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.processing.N2oModule;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

import java.util.Map;
import java.util.Set;

public class N2oSecurityModule extends N2oModule {

    private SecurityProvider securityProvider;

    public N2oSecurityModule(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public void processAction(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        securityProvider.checkAccess(getSecurityObjectMap(requestInfo.getOperation()), requestInfo.getUser());
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.getUpload().equals(UploadType.query)) {
            Map<String, Security.SecurityObject> securityObjectMap = getSecurityObjectMap(requestInfo.getQuery());
            if (securityObjectMap != null) {
                securityProvider.checkAccess(securityObjectMap, requestInfo.getUser());
                if (requestInfo.getSize() != 1) {
                    Set<Restriction> filters = securityProvider.collectRestrictions(getSecurityFilters(requestInfo.getQuery()), requestInfo.getUser());
                    requestInfo.getCriteria().getRestrictions().addAll(filters);
                }
            }
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        if (requestInfo.getUpload().equals(UploadType.query) && requestInfo.getSize() == 1) {
            securityProvider.checkAccess(getSecurityObjectMap(requestInfo.getQuery()), requestInfo.getUser());
            // todo проверка результата на соответсвие фильтрам
        }
    }


    private Map<String, Security.SecurityObject> getSecurityObjectMap(PropertiesAware propertiesAware) {
        Map<String, Object> properties = propertiesAware.getProperties();
        if (properties == null || !properties.containsKey("security")
                || ((Security) properties.get("security")).getSecurityMap() == null)
            return null;
        return ((Security) properties.get("security")).getSecurityMap();
    }

    private SecurityFilters getSecurityFilters(PropertiesAware propertiesAware) {
        Map<String, Object> properties = propertiesAware.getProperties();
        if (properties == null || !properties.containsKey("securityFilter"))
            return null;
        return (SecurityFilters) properties.get("securityFilter");
    }
}
