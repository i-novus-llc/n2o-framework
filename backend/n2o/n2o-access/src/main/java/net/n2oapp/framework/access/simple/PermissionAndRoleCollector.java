package net.n2oapp.framework.access.simple;

import net.n2oapp.framework.access.api.model.filter.N2oAccessFilter;
import net.n2oapp.framework.access.functions.TripleFunction;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.api.StringUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * Сбор точек доступа по ролям, правам пользователя
 */
@SuppressWarnings("unchecked")
public class PermissionAndRoleCollector {

    public final static BiFunction<String, String, Predicate<N2oObjectAccessPoint>> OBJECT_ACCESS = (objectId, actionId) ->
            ac -> StringUtils.maskMatch(ac.getObjectId(), objectId) &&
                    ((ac.getAction() == null && "read".equals(actionId)) || StringUtils.maskMatch(ac.getAction(), actionId));

    public final static Function<String, Predicate<N2oUrlAccessPoint>> URL_ACCESS = pattern -> ac -> ac.getMatcher().matches(pattern);

    public final static Function<String, Predicate<N2oPageAccessPoint>> PAGE_ACCESS = pageId -> ac -> ac.getPage().equals(pageId);

    public final static Function<String, Predicate<N2oReferenceAccessPoint>> REFERENCE_ACCESS = objectId -> ac -> ac.getObjectId().equals(objectId);

    public final static TripleFunction<String, String, String, Predicate<N2oColumnAccessPoint>> COLUMN_ACCESS = (pageId, containerId, columnId) ->
            ac -> ac.getPageId().equals(pageId) && ac.getContainerId().equals(containerId) && ac.getColumnId().equals(columnId);

    public final static BiFunction<String, String, Predicate<N2oFilterAccessPoint>> FILTER_ACCESS = (queryId, filterId) ->
            ac -> ac.getQueryId().equals(queryId) && ac.getFilterId().equals(filterId);

    /**
     * Собирает все элементы доступа типа role
     *
     * @param type      тип обрабатываемой единицы
     * @param predicate функция проверки обрабатываемой единицы на соответствие заданным условиям
     * @param <A>
     * @return
     */
    public static <A extends AccessPoint> List<N2oRole> collectRoles(Class<A> type, Predicate<A> predicate, SimpleCompiledAccessSchema schema) {
        return collect(() -> schema.getN2oRoles(), N2oRole::getAccessPoints, type, predicate);
    }

    /**
     * Собирает все элементы доступа типа permission
     *
     * @param type      тип обрабатываемой единицы
     * @param predicate функция проверки обрабатываемой единицы на соответствие заданным условиям
     * @param <A>
     * @return
     */
    public static <A extends AccessPoint> List<N2oPermission> collectPermission(Class<A> type, Predicate<A> predicate, SimpleCompiledAccessSchema schema) {
        return collect(() -> schema.getN2oPermissions(), N2oPermission::getAccessPoints, type, predicate);
    }

    /**
     * Собирает все элементы доступа типа user
     *
     * @param type      тип обрабатываемой единицы
     * @param predicate функция проверки обрабатываемой единицы на соответствие заданным условиям
     * @param <A>
     * @return
     */
    public static <A extends AccessPoint> List<N2oUserAccess> collectUsers(Class<A> type, Predicate<A> predicate, SimpleCompiledAccessSchema schema) {
        return collect(() -> schema.getN2oUserAccesses(), N2oUserAccess::getAccessPoints, type, predicate);
    }

    /**
     * @param supplier  список сколлекционированных элементов
     * @param getter    список access points
     * @param type      тип обрабатываемых единиц
     * @param predicate функция проверки обрабатываемых единиц на соответствие заданным условиям
     * @param <T>
     * @param <A>
     * @return
     */
    public static <T, A extends AccessPoint> List<T> collect(Supplier<List<T>> supplier, Function<T, AccessPoint[]> getter,
                                                             Class<A> type, Predicate<A> predicate) {
        List<T> ts = supplier.get();
        if (ts == null || ts.size() == 0) {
            return Collections.emptyList();
        }
        return ts.stream().filter(p -> stream(getter.apply(p))
                .filter(type::isInstance)
                .map(type::cast)
                .anyMatch(predicate))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает все фильтры доступа по объекту и действию
     *
     * @param rolePredicate       функция проверки обрабатываемых единиц(role) на соответствие заданным условиям
     * @param permissionPredicate функция проверки обрабатываемых единиц(permission) на соответствие заданным условиям
     * @param userPredicate       функция проверки обрабатываемых единиц(user) на соответствие заданным условиям
     * @param objectId            id проверяемого объекта
     * @param actionId            id проверяемого действия
     * @return фильтры доступа
     */
    public static List<N2oAccessFilter> collectFilters(Predicate<N2oRole> rolePredicate, Predicate<N2oPermission> permissionPredicate,
                                                       Predicate<N2oUserAccess> userPredicate, String objectId,
                                                       String actionId, SimpleCompiledAccessSchema schema) {
        List<N2oRole> roles = collectRoles(N2oObjectAccessPoint.class, PermissionAndRoleCollector.OBJECT_ACCESS.apply(objectId, actionId), schema)
                .stream().filter(rolePredicate).collect(Collectors.toList());
        List<N2oPermission> permissions = collectPermission(N2oObjectAccessPoint.class,
                PermissionAndRoleCollector.OBJECT_ACCESS.apply(objectId, actionId), schema)
                .stream().filter(permissionPredicate).collect(Collectors.toList());
        List<N2oUserAccess> users = collectUsers(N2oObjectAccessPoint.class, PermissionAndRoleCollector.OBJECT_ACCESS.apply(objectId, actionId), schema)
                .stream().filter(userPredicate).collect(Collectors.toList());
        List<N2oAccessFilter> filters = new ArrayList<>();
        filters.addAll(collectFilters(roles, N2oRole::getAccessPoints, objectId, actionId));
        filters.addAll(collectFilters(permissions, N2oPermission::getAccessPoints, objectId, actionId));
        filters.addAll(collectFilters(users, N2oUserAccess::getAccessPoints, objectId, actionId));
        return filters;
    }

    private static <T> List<N2oAccessFilter> collectFilters(List<T> list, Function<T, AccessPoint[]> getter, String objectId, String actionId) {
        return list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .flatMap(Arrays::stream)
                .filter(N2oObjectAccessPoint.class::isInstance)
                .map(N2oObjectAccessPoint.class::cast)
                .filter(o -> o.getObjectId().equals(objectId) && o.getAction().equals(actionId))
                .filter(o -> o.getAccessFilters() != null)
                .flatMap(o -> o.getAccessFiltersAsList().stream())
                .map(ac -> {
                    if (ac.getValues() != null && ac.getValues().length>0) {
                        return new N2oAccessFilter(ac.getFieldId(), Arrays.asList(ac.getValues()), ac.getType());
                    } else {
                        return new N2oAccessFilter(ac.getFieldId(), ac.getValue(), ac.getType());
                    }
                })
                .collect(Collectors.toList());
    }
}
