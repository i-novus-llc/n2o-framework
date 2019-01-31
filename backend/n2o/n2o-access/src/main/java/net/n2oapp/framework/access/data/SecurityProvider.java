package net.n2oapp.framework.access.data;

import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.user.UserContext;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class SecurityProvider {

    private PermissionApi permissionApi;

    public SecurityProvider(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }

    public void checkAccess(PropertiesAware propertiesAware, UserContext userContext) {
        Map<String, Object> properties = propertiesAware.getProperties();

        if (properties == null || !properties.containsKey("security")
                || ((Security) properties.get("security")).getSecurityMap() == null)
            return;
        for (Security.SecurityObject securityObject : ((Security) properties.get("security")).getSecurityMap().values()) {
            check(userContext, securityObject);
        }
    }

    /**
     * Вызывает исключение, если доступ ограничен
     */
    private void check(UserContext userContext, Security.SecurityObject securityObject) {
        if (securityObject.getDenied() != null && securityObject.getDenied())
            throw new UnauthorizedException();
        if (securityObject.getPermitAll() != null && securityObject.getPermitAll())
            return;

        if (!permissionApi.hasAuthentication(userContext)) {
            if (securityObject.getAnonymous() != null && securityObject.getAnonymous())
                return;
            throw new UnauthorizedException();
        } else {
            if (securityObject.getAuthenticated() != null && securityObject.getAuthenticated())
                return;
            if (securityObject.getAnonymous() != null && securityObject.getAnonymous())
                throw new AccessDeniedException();
        }

        if (!checkAccessList(userContext, securityObject.getRoles(), permissionApi::hasRole)
                && !checkAccessList(userContext, securityObject.getPermissions(), permissionApi::hasPermission)
                && !checkAccessList(userContext, securityObject.getUsernames(), permissionApi::hasUsername))
            throw new AccessDeniedException();
    }

    private boolean checkAccessList(UserContext userContext, Set<String> accessList, BiPredicate<UserContext, String> f) {
        if (accessList == null) return false;
        for (String param : accessList) {
            if (f.test(userContext, param)) return true;
        }
        return false;
    }
}