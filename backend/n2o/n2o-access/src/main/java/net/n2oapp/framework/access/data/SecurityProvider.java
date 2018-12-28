package net.n2oapp.framework.access.data;

import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.RequestInfo;
import net.n2oapp.framework.api.user.UserContext;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public class SecurityProvider {

    private PermissionApi permissionApi;

    public SecurityProvider(PermissionApi permissionApi) {

        this.permissionApi = permissionApi;
    }

    public void checkAccess(RequestInfo requestInfo) {
        Map<String, Object> properties = null;
        if (requestInfo instanceof ActionRequestInfo) {
            ActionRequestInfo actionRequestInfo = (ActionRequestInfo) requestInfo;
            CompiledObject.Operation operation = actionRequestInfo.getOperation();
            properties = operation.getProperties();
        } else if (requestInfo instanceof QueryRequestInfo) {
            QueryRequestInfo queryRequestInfo = (QueryRequestInfo) requestInfo;
            properties = queryRequestInfo.getQuery().getProperties();
        }

        if (properties == null
                || properties.size() == 0
                || properties.get("security") == null
                || ((Security) properties.get("security")).getSecurityMap() == null)
            return;
        for (Security.SecurityObject securityObject : ((Security) properties.get("security")).getSecurityMap().values()) {
            check(requestInfo, securityObject);
        }
    }

    /**
     * Вызывает исключение, если доступ ограничен
     */
    private void check(RequestInfo requestInfo, Security.SecurityObject securityObject) {
        if (securityObject.getDenied() != null && securityObject.getDenied())
            throw new UnauthorizedException();
        if (securityObject.getPermitAll() != null && securityObject.getPermitAll())
            return;

        if (!permissionApi.hasAuthentication(requestInfo.getUser())) {
            if (securityObject.getAnonymous() != null && securityObject.getAnonymous())
                return;
            throw new UnauthorizedException();
        } else {
            if (securityObject.getAuthenticated() != null && securityObject.getAuthenticated())
                return;
            if (securityObject.getAnonymous() != null && securityObject.getAnonymous())
                throw new AccessDeniedException();

        }

        if (!checkAccessList(requestInfo.getUser(), securityObject.getRoles(), permissionApi::hasRole)
                && !checkAccessList(requestInfo.getUser(), securityObject.getPermissions(), permissionApi::hasPermission)
                && !checkAccessList(requestInfo.getUser(), securityObject.getUsernames(), permissionApi::hasUsername))
            throw new AccessDeniedException();
    }

    private boolean checkAccessList(UserContext userContext, List<String> accessList, BiPredicate<UserContext, String> f) {
        if (accessList == null) return false;
        for (String param : accessList) {
            if (f.test(userContext, param)) return true;
        }
        return false;
    }
}