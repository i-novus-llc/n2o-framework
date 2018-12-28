package net.n2oapp.framework.access.data;

import net.n2oapp.framework.access.api.model.Permission;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.api.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.api.AuthorizationApi;
import net.n2oapp.framework.access.api.AuthorizationApiProvider;
import net.n2oapp.framework.access.api.model.ObjectPermission;
import net.n2oapp.framework.access.api.model.filter.N2oAccessFilter;
import net.n2oapp.framework.access.exception.N2oSecurityException;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.ui.QueryRequestInfo;

import java.util.*;

/**
 * Проверка доступа к данным
 */
public class DataAccessChecker {
    private AuthorizationApi authorizationApi;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DataAccessChecker(AuthorizationApiProvider serviceProvider) {
        this.authorizationApi = serviceProvider.getAuthorizationApi();
    }


    public void checkAction(UserContext user, CompiledObject.Operation action, DataSet dataSet)
            throws N2oSecurityException {
        String objectId = action.getObjectId();
        logger.debug("Checking user's rights for executing action (userName:{},objectId:{},actionId:{})",
                user, objectId, action.getId());
        ObjectPermission permission = authorizationApi.getPermissionForObject(user, objectId, action.getId());
        permission.resolveToException();
        if (permission.getAccessFilters() != null)
            checkDataSet(dataSet, permission.getAccessFilters(), action);
    }


    public void checkQuery(UserContext user, QueryRequestInfo requestInfo) throws N2oSecurityException {
        CompiledQuery query = requestInfo.getQuery();
        N2oPreparedCriteria criteria = requestInfo.getCriteria();
        CompiledObject object = query.getObject();
        if (object != null) {
            String objectId = object.getId();
            logger.debug("Checking user's rights for executing query (userName:{},objectId:{},queryId:{})",
                    user, objectId, query.getId());

            ObjectPermission permission = getN2oPermission(user, requestInfo, objectId);

//            if (permission.getAccessFilters() != null)
//                checkCriteria(criteria, permission.getAccessFilters(), query);

            Optional<Permission> denied = requestInfo.getData().keySet().stream()
                    .map(filter -> authorizationApi.getPermissionForFilter(user, query.getId(), filter))
                    .filter(p -> !p.isAllowed()).findFirst();
            if (denied.isPresent()) {
                denied.get().resolveToException();
            }
        }
    }

    private ObjectPermission getN2oPermission(UserContext user, QueryRequestInfo requestInfo, String objectId) {
        ObjectPermission permission;
        permission = authorizationApi.getPermissionForObject(user, objectId, "read");
        permission.resolveToException();
        return permission;
    }


    public void checkCollectionPage(UserContext user, CollectionPage<DataSet> page, QueryRequestInfo requestInfo) {
        CompiledQuery query = requestInfo.getQuery();
        String objectId = query.getObject().getId();
        logger.debug(
                "Checking user's rights for access to query executing result (userName:{},objectId:{},queryId:{})",
                user, objectId, query.getId());
        ObjectPermission permission = getN2oPermission(user, requestInfo, objectId);
        if (permission.getAccessFilters() != null)
            for (DataSet dataSet : page.getCollection()) {
                checkDataSet(dataSet, permission.getAccessFilters(), query);
            }
    }


    private void checkDataSet(DataSet dataSet, List<N2oAccessFilter> accessFilters, CompiledObject.Operation action) {
        //todo
    }

    private void checkDataSet(DataSet dataSet, List<N2oAccessFilter> accessFilters, CompiledQuery query) {
        //todo
    }


}
