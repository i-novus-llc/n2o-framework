package net.n2oapp.framework.access.data;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.criteria.filters.Filter;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Сервис для проверки наличия прав доступа у пользователя
 */
public class SecurityProvider {
    private final PermissionApi permissionApi;
    private final boolean strictFiltering;
    private static final Logger logger = LoggerFactory.getLogger(SecurityProvider.class);

    public SecurityProvider(PermissionApi permissionApi, boolean strictFiltering) {
        this.permissionApi = permissionApi;
        this.strictFiltering = strictFiltering;
    }

    /**
     * Проверка есть ли у пользователя из userContext доступ к объекту, права к которуму регулирует security
     *
     * @param security    права доступа для проверки
     * @param userContext информация о пользователе
     */
    public void checkAccess(Security security, UserContext userContext) {
        if (CollectionUtils.isEmpty(security))
            return;
        for (SecurityObject securityObject : security.get(0).values()) {
            check(userContext, securityObject);
        }
    }

    /**
     * Проверяет наличие доступа без выброса исключения. Используется для фильтрации полей.
     *
     * @param security    права доступа
     * @param userContext контекст пользователя
     * @return true если доступ разрешён
     */
    public boolean hasFieldAccess(Security security, UserContext userContext) {
        try {
            checkAccess(security, userContext);
            return true;
        } catch (AccessDeniedException | UnauthorizedException e) {
            return false;
        }
    }

    /**
     * Сборка ограничений прав доступа актуальных для пользователя из userContext из общего списка фильтров
     *
     * @param securityFilters фильтрация объекта
     * @param userContext     информация о пользователе
     * @return список ограничений прав доступа к объекту
     */
    public List<Restriction> collectRestrictions(SecurityFilters securityFilters, UserContext userContext) {
        if (securityFilters == null)
            return Collections.emptyList();
        Set<N2oObjectFilter> filters = new HashSet<>();
        Set<String> removeFilters = new HashSet<>();
        addIfNotNull(filters, securityFilters.getPermitAllFilters());
        addIfNotNull(removeFilters, securityFilters.getRemovePermitAllFilters());
        if (permissionApi.hasAuthentication(userContext)) {
            addIfNotNull(filters, securityFilters.getAuthenticatedFilters());
            addIfNotNull(removeFilters, securityFilters.getRemoveAuthenticatedFilters());
        } else {
            addIfNotNull(filters, securityFilters.getAnonymousFilters());
            addIfNotNull(removeFilters, securityFilters.getRemoveAnonymousFilters());
        }
        addAccessible(filters, securityFilters.getRoleFilters(), userContext, permissionApi::hasRole);
        addAccessible(removeFilters, securityFilters.getRemoveRoleFilters(), userContext, permissionApi::hasRole);
        addAccessible(filters, securityFilters.getPermissionFilters(), userContext, permissionApi::hasPermission);
        addAccessible(removeFilters, securityFilters.getRemovePermissionFilters(), userContext, permissionApi::hasPermission);
        addAccessible(filters, securityFilters.getUserFilters(), userContext, permissionApi::hasUsername);
        addAccessible(removeFilters, securityFilters.getRemoveUserFilters(), userContext, permissionApi::hasUsername);
        filters.removeIf(f -> removeFilters.contains(f.getId()));
        return filters.stream().map(this::restriction).toList();
    }

    private static <T> void addIfNotNull(Set<T> target, Collection<T> source) {
        if (source != null)
            target.addAll(source);
    }

    private static <T> void addAccessible(Set<T> target, Map<String, ? extends Collection<T>> source,
                                          UserContext userContext, BiPredicate<UserContext, String> access) {
        if (source == null)
            return;
        source.forEach((key, values) -> {
            if (access.test(userContext, key))
                target.addAll(values);
        });
    }

    private Restriction restriction(N2oObjectFilter filter) {
        Object value = filter.isArray() ? Arrays.asList(filter.getValues()) : filter.getValue();
        return new Restriction(filter.getId(), filter.getFieldId(), value, filter.getType());
    }

    /**
     * Вызывает исключение, если данные операции не удовлетворяют фильтрам доступа
     *
     * @param data            Данные
     * @param securityFilters Фильтры доступа
     * @param userContext     Контекст пользователя
     */
    public void checkObjectRestrictions(DataSet data, SecurityFilters securityFilters,
                                        UserContext userContext) {
        checkRestrictions(securityFilters, userContext, r -> data.get(r.getId()));
    }

    /**
     * Вызывает исключение, если данные выборки не удовлетворяют фильтрам доступа
     *
     * @param data            Данные
     * @param securityFilters Фильтры доступа
     * @param userContext     Контекст пользователя
     */
    public void checkQueryRestrictions(DataSet data, SecurityFilters securityFilters,
                                       UserContext userContext, Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> filtersMap) {
        checkRestrictions(securityFilters, userContext, r -> {
            if (filtersMap.get(r.getFieldId()) == null) {
                logger.warn("В схеме доступа указан фильтр {}, но его нет в выборке", r.getFieldId());
                return null;
            }
            N2oQuery.Filter filter = filtersMap.get(r.getFieldId()).get(r.getType());
            return filter != null ? data.get(filter.getFilterId()) : null;
        });
    }

    /**
     * Вызывает исключение, если данные не удовлетворяют фильтрам доступа
     *
     * @param securityFilters   Фильтры доступа
     * @param userContext       Контекст пользователя
     * @param realValueFunction Функция, получающая реальное значение фильтра
     */
    private void checkRestrictions(SecurityFilters securityFilters,
                                   UserContext userContext,
                                   Function<Restriction, Object> realValueFunction) {
        List<Restriction> restrictions = collectRestrictions(securityFilters, userContext);
        ContextProcessor contextProcessor = new ContextProcessor(userContext);
        for (Restriction securityRestriction : restrictions) {
            Object realValue = realValueFunction.apply(securityRestriction);
            if (realValue != null || strictFiltering) {
                if (FilterTypeEnum.ArityEnum.NULLARY.equals(securityRestriction.getType().arity)) {
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
     *
     * @param fieldId        идентификатор поля
     * @param realValue      значение
     * @param securityFilter фильтр поля
     */
    private void checkByField(String fieldId, Object realValue, Filter securityFilter) {
        if (!securityFilter.check(realValue))
            throw new AccessDeniedException("Access denied by field " + fieldId);
    }

    /**
     * Вызывает исключение, если доступ ограничен
     */
    private void check(UserContext userContext, SecurityObject securityObject) {
        if (Boolean.TRUE.equals(securityObject.getDenied()))
            throw new UnauthorizedException();
        if (Boolean.TRUE.equals(securityObject.getPermitAll()))
            return;

        if (!permissionApi.hasAuthentication(userContext)) {
            if (Boolean.TRUE.equals(securityObject.getAnonymous()))
                return;
            throw new UnauthorizedException();
        }
        if (Boolean.TRUE.equals(securityObject.getAuthenticated()))
            return;
        if (Boolean.TRUE.equals(securityObject.getAnonymous()))
            throw new AccessDeniedException();

        if (!(checkAccessList(userContext, securityObject.getRoles(), permissionApi::hasRole)
                || checkAccessList(userContext, securityObject.getPermissions(), permissionApi::hasPermission)
                || checkAccessList(userContext, securityObject.getUsernames(), permissionApi::hasUsername)))
            throw new AccessDeniedException();
    }

    private boolean checkAccessList(UserContext userContext, Set<String> accessList, BiPredicate<UserContext, String> f) {
        if (accessList == null)
            return false;

        for (String param : accessList)
            if (f.test(userContext, param))
                return true;
        return false;
    }
}