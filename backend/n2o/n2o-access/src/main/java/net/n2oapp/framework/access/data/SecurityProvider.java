package net.n2oapp.framework.access.data;

import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.user.UserContext;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SecurityProvider {

    private PermissionApi permissionApi;

    public SecurityProvider(PermissionApi permissionApi) {
        this.permissionApi = permissionApi;
    }

    public void checkAccess(Map<String, Security.SecurityObject> securityObjectMap, UserContext userContext) {
        if (securityObjectMap == null)
            return;
        for (Security.SecurityObject securityObject : securityObjectMap.values()) {
            check(userContext, securityObject);
        }
    }

    public Set<Restriction> collectRestrictions(SecurityFilters securityFilters, UserContext userContext) {
        if (securityFilters == null)
            return null;
        Set<N2oObjectFilter> filters = new HashSet<>();
        Set<String> removeFilters = new HashSet<>();
        if (securityFilters.getPermitAllFilters() != null) {
            filters.addAll(securityFilters.getPermitAllFilters());
        }
        if (securityFilters.getRemovePermitAllFilters() != null) {
            removeFilters.addAll(securityFilters.getRemovePermitAllFilters());
        }
        if (permissionApi.hasAuthentication(userContext)) {
            if (securityFilters.getAuthenticatedFilters() != null) {
                filters.addAll(securityFilters.getAuthenticatedFilters());
            }
            if (securityFilters.getRemoveAuthenticatedFilters() != null) {
                removeFilters.addAll(securityFilters.getRemoveAuthenticatedFilters());
            }
        } else {
            if (securityFilters.getAnonymousFilters() != null) {
                filters.addAll(securityFilters.getAnonymousFilters());
            }
            if (securityFilters.getRemoveAnonymousFilters() != null) {
                removeFilters.addAll(securityFilters.getRemoveAnonymousFilters());
            }
        }
        if (securityFilters.getRoleFilters() != null) {
            securityFilters.getRoleFilters().keySet().stream().filter(r -> permissionApi.hasRole(userContext, r))
                    .forEach(r -> filters.addAll(securityFilters.getRoleFilters().get(r)));
        }
        if (securityFilters.getRemoveRoleFilters() != null) {
            securityFilters.getRemoveRoleFilters().keySet().stream().filter(r -> permissionApi.hasRole(userContext, r))
                    .forEach(r -> removeFilters.addAll(securityFilters.getRemoveRoleFilters().get(r)));
        }
        if (securityFilters.getPermissionFilters() != null) {
            securityFilters.getPermissionFilters().keySet().stream().filter(p -> permissionApi.hasPermission(userContext, p))
                    .forEach(p -> filters.addAll(securityFilters.getPermissionFilters().get(p)));
        }
        if (securityFilters.getRemovePermissionFilters() != null) {
            securityFilters.getRemovePermissionFilters().keySet().stream().filter(p -> permissionApi.hasPermission(userContext, p))
                    .forEach(p -> removeFilters.addAll(securityFilters.getRemovePermissionFilters().get(p)));
        }
        if (securityFilters.getUserFilters() != null) {
            securityFilters.getUserFilters().keySet().stream().filter(u -> permissionApi.hasUsername(userContext, u))
                    .forEach(u -> filters.addAll(securityFilters.getUserFilters().get(u)));
        }
        if (securityFilters.getRemoveUserFilters() != null) {
            securityFilters.getRemoveUserFilters().keySet().stream().filter(u -> permissionApi.hasUsername(userContext, u))
                    .forEach(u -> removeFilters.addAll(securityFilters.getRemoveUserFilters().get(u)));
        }
        filters.removeIf(f -> removeFilters.contains(f.getId()));
        return filters.stream().map(f -> new Restriction(f.getFieldId(), f.getValue(), f.getType())).collect(Collectors.toSet());
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