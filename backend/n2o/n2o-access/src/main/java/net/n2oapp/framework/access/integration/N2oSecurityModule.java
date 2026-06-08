package net.n2oapp.framework.access.integration;

import lombok.Setter;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;

import net.n2oapp.framework.api.user.UserContext;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.FIELD_SECURITY_PROP_NAME;
import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static net.n2oapp.framework.access.metadata.SecurityFilters.SECURITY_FILTERS_PROP_NAME;

/**
 * Проверка прав доступа на вызов действий и выборок N2O
 */
@Setter
public class N2oSecurityModule implements DataProcessing {

    private SecurityProvider securityProvider;
    private boolean filteringForUnique;

    public N2oSecurityModule(SecurityProvider securityProvider, boolean filteringForUnique) {
        this.securityProvider = securityProvider;
        this.filteringForUnique = filteringForUnique;
    }

    @Override
    public void processAction(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        securityProvider.checkAccess(getSecurityObject(requestInfo.getOperation()), requestInfo.getUser());
        securityProvider.checkObjectRestrictions(dataSet, getSecurityFilters(requestInfo.getOperation()),
                requestInfo.getUser());
        // не отправляем недоступные in-поля в запрос на изменение
        filterFields(dataSet,
                getFieldSecurity(requestInfo.getOperation(), Security.IN_FIELD_SECURITY_PROP_NAME),
                requestInfo.getUser());
    }

    @Override
    public void processActionResult(ActionRequestInfo requestInfo, ActionResponseInfo responseInfo, DataSet dataSet) {
        if (dataSet == null) return;
        UserContext user = requestInfo.getUser();
        // недоступные in-поля не возвращаем пользователю
        filterFields(dataSet, getFieldSecurity(requestInfo.getOperation(), Security.IN_FIELD_SECURITY_PROP_NAME), user);
        // недоступные out-поля не возвращаем в ответе
        filterFields(dataSet, getFieldSecurity(requestInfo.getOperation(), Security.OUT_FIELD_SECURITY_PROP_NAME), user);
    }

    private void filterFields(DataSet dataSet, Map<String, Security> fieldSecurity, UserContext user) {
        if (dataSet == null || fieldSecurity == null || fieldSecurity.isEmpty()) return;
        fieldSecurity.forEach((fieldId, security) -> {
            if (!securityProvider.hasFieldAccess(security, user)) removeField(dataSet, fieldId);
        });
    }

    private static void removeField(DataSet dataSet, String fieldId) {
        int dot = fieldId.indexOf('.');
        if (dot < 0) {
            dataSet.remove(fieldId);
            return;
        }
        Object parent = dataSet.get(fieldId.substring(0, dot));
        String child = fieldId.substring(dot + 1);
        if (parent instanceof DataSet nested)
            removeField(nested, child);
        else if (parent instanceof List<?> list)
            for (Object item : list)
                if (item instanceof DataSet itemDs)
                    removeField(itemDs, child);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Security> getFieldSecurity(PropertiesAware propertiesAware, String propName) {
        Map<String, Object> props = propertiesAware.getProperties();
        if (props == null || !props.containsKey(propName)) return null;
        return (Map<String, Security>) props.get(propName);
    }

    @Override
    public void processQuery(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo) {
        if (requestInfo.getMode().equals(DefaultValuesModeEnum.QUERY)) {
            Security security = getSecurityObject(requestInfo.getQuery());
            if (security != null) {
                securityProvider.checkAccess(security, requestInfo.getUser());
                if (requestInfo.getSize() != 1 || filteringForUnique) {
                    List<Restriction> securityFilters = securityProvider.collectRestrictions(
                            getSecurityFilters(requestInfo.getQuery()), requestInfo.getUser()
                    );
                    requestInfo.getCriteria().addRestrictions(securityFilters);
                }
            }
        }
    }

    @Override
    public void processQueryResult(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, CollectionPage<DataSet> page) {
        if (DefaultValuesModeEnum.QUERY.equals(requestInfo.getMode())
                && requestInfo.getSize() == 1) {
            DataSet data = page.getCollection().iterator().next();
            securityProvider.checkAccess(getSecurityObject(requestInfo.getQuery()), requestInfo.getUser());
            securityProvider.checkQueryRestrictions(data, getSecurityFilters(requestInfo.getQuery()),
                    requestInfo.getUser(), requestInfo.getQuery().getFiltersMap());
        }

        Map<String, Security> fieldSecurity = getFieldSecurity(requestInfo.getQuery(), FIELD_SECURITY_PROP_NAME);
        if (fieldSecurity != null && !fieldSecurity.isEmpty()) {
            for (DataSet row : page.getCollection()) {
                filterFields(row, fieldSecurity, requestInfo.getUser());
            }
        }
    }

    private Security getSecurityObject(PropertiesAware propertiesAware) {
        Map<String, Object> properties = propertiesAware.getProperties();
        if (properties == null || !properties.containsKey(SECURITY_PROP_NAME)
                || properties.get(SECURITY_PROP_NAME) == null)
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
