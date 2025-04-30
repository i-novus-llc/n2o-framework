package net.n2oapp.framework.access.simple;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.access.functions.TripleFunction;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.api.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Arrays.stream;

/**
 * Сбор точек доступа по ролям, правам пользователя
 */
@SuppressWarnings("unchecked")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionAndRoleCollector {

    public static final BiFunction<String, String, Predicate<N2oObjectAccessPoint>> OBJECT_ACCESS = (objectId, actionId) ->
            ac -> StringUtils.maskMatch(ac.getObjectId(), objectId) &&
                    (actionId == null || StringUtils.maskMatch(ac.getAction(), actionId));

    public static final Function<String, Predicate<N2oUrlAccessPoint>> URL_ACCESS = pattern -> ac -> ac.getMatcher().matches(pattern);

    public static final Function<String, Predicate<N2oPageAccessPoint>> PAGE_ACCESS = pageId -> ac -> ac.getPage().equals(pageId);

    public static final Function<String, Predicate<N2oReferenceAccessPoint>> REFERENCE_ACCESS = objectId -> ac -> ac.getObjectId().equals(objectId);

    public static final TripleFunction<String, String, String, Predicate<N2oColumnAccessPoint>> COLUMN_ACCESS = (pageId, containerId, columnId) ->
            ac -> ac.getPageId().equals(pageId) && ac.getContainerId().equals(containerId) && ac.getColumnId().equals(columnId);

    public static final BiFunction<String, String, Predicate<N2oFilterAccessPoint>> FILTER_ACCESS = (queryId, filterId) ->
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
        return collect(schema::getN2oRoles, N2oRole::getAccessPoints, type, predicate);
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
        return collect(schema::getN2oPermissions, N2oPermission::getAccessPoints, type, predicate);
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
        return collect(schema::getN2oUserAccesses, N2oUserAccess::getAccessPoints, type, predicate);
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
        if (ts == null || ts.isEmpty()) {
            return Collections.emptyList();
        }
        return ts.stream().filter(p -> stream(getter.apply(p))
                        .filter(type::isInstance)
                        .map(type::cast)
                        .anyMatch(predicate))
                .toList();
    }
}
