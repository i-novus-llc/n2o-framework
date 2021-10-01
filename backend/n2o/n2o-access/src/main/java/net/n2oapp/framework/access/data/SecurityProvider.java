package net.n2oapp.framework.access.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.user.UserContext;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Сервис для проверки наличия прав доступа у пользователя
 */
public class SecurityProvider {
    private PermissionApi permissionApi;
    private boolean strictFiltering;

    public SecurityProvider(PermissionApi permissionApi, boolean strictFiltering) {
        this.permissionApi = permissionApi;
        this.strictFiltering = strictFiltering;
    }

    /**
     * Проверка есть ли у пользователя из userContext доступ к объекту, права к которуму регулирует security
     * @param security      права доступа для проверки
     * @param userContext   информация о пользователе
     */
    public void checkAccess(Security security, UserContext userContext) {
        if (security == null || security.getSecurityMap() == null)
            return;
        for (Security.SecurityObject securityObject : security.getSecurityMap().values()) {
            check(userContext, securityObject);
        }
    }

    /**
     * Сборка ограничений прав доступа актуальных для пользователя из userContext из общего списка фильтров
     * @param securityFilters   фильтрация объекта
     * @param userContext       информация о пользователе
     * @return  список ограничений прав доступа к объекту
     */
    public List<Restriction> collectRestrictions(SecurityFilters securityFilters, UserContext userContext) {
        if (securityFilters == null)
            return Collections.emptyList();
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
        return filters.stream().map(this::restriction).collect(Collectors.toList());
    }

    private Restriction restriction(N2oObjectFilter filter) {
        Object value = filter.isArray() ? Arrays.asList(filter.getValues()) : filter.getValue();
        return new Restriction(filter.getFieldId(), value, filter.getType());
    }

    /**
     * Вызывает исключение, если данные не удовлетворяют фильтрам доступа
     * @param data Данные
     * @param securityFilters Фильтры доступа
     * @param userContext Контекст пользователя
     */
    public void checkRestrictions(DataSet data, SecurityFilters securityFilters, UserContext userContext) {
        List<Restriction> restrictions = collectRestrictions(securityFilters, userContext);
        ContextProcessor contextProcessor = new ContextProcessor(userContext);
        for (Restriction securityRestriction : restrictions) {
            Object realValue = data.get(securityRestriction.getFieldId());
            if (realValue != null || strictFiltering) {
                if (FilterType.Arity.nullary.equals(securityRestriction.getType().arity)) {
                    Filter securityFilter = new Filter(securityRestriction.getType());
                    checkByField(securityRestriction.getFieldId(), realValue, securityFilter);
                } else {
                    Object filterValue = contextProcessor.resolve(securityRestriction.getValue());
                    if (filterValue != null) {
                        Filter securityFilter = new Filter(filterValue, securityRestriction.getType());
                        checkByField(securityRestriction.getFieldId(), realValue, securityFilter);
                    }
                }
            }
        }
    }

    /**
     * Проверка фильтра по полю
     * @param fieldId   идентификатор поля
     * @param realValue     значение
     * @param securityFilter    фильтр поля
     */
    private void checkByField(String fieldId, Object realValue, Filter securityFilter) {
        if (!securityFilter.check(realValue))
            throw new AccessDeniedException("Access denied by field " + fieldId);
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