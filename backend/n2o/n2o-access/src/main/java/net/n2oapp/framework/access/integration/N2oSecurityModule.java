package net.n2oapp.framework.access.integration;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static net.n2oapp.framework.access.metadata.SecurityFilters.SECURITY_FILTERS_PROP_NAME;

/**
 * Проверка прав доступа на вызов действий и выборок N2O
 */
public class N2oSecurityModule implements DataProcessing {

    private SecurityProvider securityProvider;

    public N2oSecurityModule(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }

    @Override
    public void processAction(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        securityProvider.checkAccess(getSecurityObject(requestInfo.getOperation()), requestInfo.getUser());
        securityProvider.checkRestrictions(dataSet, getSecurityFilters(requestInfo.getOperation()), requestInfo.getUser());
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.getUpload().equals(UploadType.query)) {
            Security security = getSecurityObject(requestInfo.getQuery());
            if (security != null) {
                securityProvider.checkAccess(security, requestInfo.getUser());
                if (requestInfo.getSize() != 1) {
                    List<Restriction> securityFilters = securityProvider.collectRestrictions(getSecurityFilters(requestInfo.getQuery()), requestInfo.getUser());
                    requestInfo.getCriteria().addRestrictions(securityFilters);
                }
            }
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        if (requestInfo.getUpload().equals(UploadType.query)
                && requestInfo.getSize() == 1
                && UploadType.query.equals(requestInfo.getUpload())) {
            DataSet data = page.getCollection().iterator().next();
            securityProvider.checkAccess(getSecurityObject(requestInfo.getQuery()), requestInfo.getUser());
            securityProvider.checkRestrictions(data, getSecurityFilters(requestInfo.getQuery()), requestInfo.getUser());
        }
    }


    private Security getSecurityObject(PropertiesAware propertiesAware) {
        Map<String, Object> properties = propertiesAware.getProperties();
        if (properties == null || !properties.containsKey(SECURITY_PROP_NAME)
                || ((Security) properties.get(SECURITY_PROP_NAME)).getSecurityMap() == null)
            return null;
        return (Security) properties.get(SECURITY_PROP_NAME);
    }

    private SecurityFilters getSecurityFilters(PropertiesAware propertiesAware) {
        Map<String, Object> properties = propertiesAware.getProperties();
        if (properties == null || !properties.containsKey(SECURITY_FILTERS_PROP_NAME))
            return null;
        return (SecurityFilters) properties.get(SECURITY_FILTERS_PROP_NAME);
    }
}
