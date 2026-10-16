package net.n2oapp.framework.access.integration.metadata.transform;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.access.simple.PermissionAndRoleCollector;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CompiledClassAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.CompileTransformer;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static net.n2oapp.framework.access.metadata.SecurityFilters.SECURITY_FILTERS_PROP_NAME;
import static net.n2oapp.framework.access.simple.PermissionAndRoleCollector.*;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

@Getter
@Setter
public abstract class BaseAccessTransformer<D extends Compiled, C extends CompileContext<?, ?>>
        implements CompileTransformer<D, C>, CompiledClassAware {

    private static final String DEFAULT_OBJECT_ACCESS_DENIED = "n2o.access.deny_objects";
    private static final String DEFAULT_PAGE_ACCESS_DENIED = "n2o.access.deny_pages";
    private static final String DEFAULT_URL_ACCESS_DENIED = "n2o.access.deny_urls";
    private static final String CUSTOM = "custom";

    protected void collectObjectAccess(PropertiesAware compiled, String objectId,
                                       String operationId, SimpleCompiledAccessSchema schema,
                                       CompileProcessor p) {
        if (isNull(objectId)) return;
        Security security = getSecurity(compiled);
        SecurityObject securityObject = new SecurityObject();
        if (!CollectionUtils.isEmpty(security) &&
                (nonNull(security.getFirst().get("object"))
                        || nonNull(security.getFirst().get(CUSTOM)))) return;

        // Проверка точек доступа с использованием вынесенных методов
        if (hasObjectAccessPoint(schema.getPermitAllPoints(), objectId, operationId)) {
            securityObject.setPermitAll(true);
        }

        if (hasObjectAccessPoint(schema.getAuthenticatedPoints(), objectId, operationId)) {
            securityObject.setAuthenticated(true);
        }

        if (hasObjectAccessPoint(schema.getAnonymousPoints(), objectId, operationId)) {
            securityObject.setAnonymous(true);
        }

        collectAccessList(securityObject, N2oObjectAccessPoint.class, OBJECT_ACCESS.apply(objectId, operationId), schema);

        if (securityObject.isEmpty()) {
            Boolean defaultObjectAccessDenied = p.resolve(property(DEFAULT_OBJECT_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultObjectAccessDenied);
            securityObject.setDenied(defaultObjectAccessDenied);
        }
        if (Boolean.TRUE.equals(securityObject.getPermitAll())) {
            if (security.isEmpty()) compiled.getProperties().remove(SECURITY_PROP_NAME);
            return;
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.getFirst().put("object", securityObject);
    }

    private <A extends AccessPoint> void collectAccessList(SecurityObject securityObject, Class<A> type,
                                                           Predicate<A> predicate, SimpleCompiledAccessSchema schema) {
        setIds(PermissionAndRoleCollector.collectUsers(type, predicate, schema), N2oUserAccess::getId, securityObject::setUsernames);
        setIds(PermissionAndRoleCollector.collectPermission(type, predicate, schema), N2oPermission::getId, securityObject::setPermissions);
        setIds(PermissionAndRoleCollector.collectRoles(type, predicate, schema), N2oRole::getId, securityObject::setRoles);
    }

    private static <T> void setIds(List<T> items, Function<T, String> idGetter, Consumer<Set<String>> setter) {
        if (!CollectionUtils.isEmpty(items))
            setter.accept(items.stream().map(idGetter).collect(Collectors.toSet()));
    }

    protected void collectPageAccess(PropertiesAware compiled, String pageId, SimpleCompiledAccessSchema schema,
                                     CompileProcessor p) {
        if (isNull(pageId))
            return;
        final String originPageId = pageId.split("\\?")[0];
        Security security = getSecurity(compiled);
        if (!CollectionUtils.isEmpty(security) &&
                (nonNull(security.getFirst().get("page"))
                        || nonNull(security.getFirst().get(CUSTOM)))) return;

        SecurityObject securityObject = new SecurityObject();

        if (hasPageAccessPoint(schema.getPermitAllPoints(), originPageId)) {
            securityObject.setPermitAll(true);
        }

        if (hasPageAccessPoint(schema.getAuthenticatedPoints(), originPageId)) {
            securityObject.setAuthenticated(true);
        }

        if (hasPageAccessPoint(schema.getAnonymousPoints(), originPageId)) {
            securityObject.setAnonymous(true);
        }

        collectAccessList(securityObject, N2oPageAccessPoint.class, PAGE_ACCESS.apply(originPageId), schema);

        if (securityObject.isEmpty()) {
            Boolean defaultPageAccessDenied = p.resolve(property(DEFAULT_PAGE_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultPageAccessDenied);
            securityObject.setDenied(defaultPageAccessDenied);
        }
        if (Boolean.TRUE.equals(securityObject.getPermitAll())) {
            if (security.isEmpty()) compiled.getProperties().remove(SECURITY_PROP_NAME);
            return;
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.getFirst().put("page", securityObject);
    }

    protected void collectUrlAccess(PropertiesAware compiled, String url, SimpleCompiledAccessSchema schema,
                                    CompileProcessor p) {
        if (isNull(url))
            return;
        Security security = getSecurity(compiled);
        if (!CollectionUtils.isEmpty(security) &&
                (nonNull(security.getFirst().get("url"))
                        || nonNull(security.getFirst().get(CUSTOM)))) return;

        SecurityObject securityObject = new SecurityObject();

        if (hasUrlAccessPoint(schema.getPermitAllPoints(), url)) {
            securityObject.setPermitAll(true);
        }

        if (hasUrlAccessPoint(schema.getAuthenticatedPoints(), url)) {
            securityObject.setAuthenticated(true);
        }

        if (hasUrlAccessPoint(schema.getAnonymousPoints(), url)) {
            securityObject.setAnonymous(true);
        }

        collectAccessList(securityObject, N2oUrlAccessPoint.class, URL_ACCESS.apply(url), schema);

        if (securityObject.isEmpty()) {
            Boolean defaultUrlAccessDenied = p.resolve(property(DEFAULT_URL_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultUrlAccessDenied);
            securityObject.setDenied(defaultUrlAccessDenied);
        }
        if (Boolean.TRUE.equals(securityObject.getPermitAll())) {
            if (security.isEmpty()) compiled.getProperties().remove(SECURITY_PROP_NAME);
            return;
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.getFirst().put("url", securityObject);
    }

    private Security getSecurity(PropertiesAware compiled) {
        if (isNull(compiled.getProperties())) {
            compiled.setProperties(new HashMap<>());
        }
        if (isNull(compiled.getProperties().get(SECURITY_PROP_NAME))) {
            compiled.getProperties().put(SECURITY_PROP_NAME, new Security());
        }
        return (Security) compiled.getProperties().get(SECURITY_PROP_NAME);
    }

    /**
     * Возвращает плоский список всех кнопок тулбара: верхнеуровневые кнопки и элементы вложенных submenu.
     * Используется для единообразного обхода тулбара во всех access-трансформерах.
     */
    protected static List<AbstractButton> flattenButtons(List<Group> toolbar) {
        List<AbstractButton> result = new ArrayList<>();
        if (isNull(toolbar)) return result;
        for (Group group : toolbar) {
            if (isNull(group.getButtons())) continue;
            for (AbstractButton b : group.getButtons()) {
                result.add(b);
                if (b instanceof Submenu submenu && nonNull(submenu.getContent())) {
                    result.addAll(submenu.getContent());
                }
            }
        }
        return result;
    }

    // Вспомогательные методы для проверки наличия точек доступа

    private boolean hasObjectAccessPoint(List<AccessPoint> points, String objectId, String operationId) {
        return nonNull(points) && points.stream()
                .anyMatch(ap -> ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                        && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                        && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId)));
    }

    private boolean hasPageAccessPoint(List<AccessPoint> points, String pageId) {
        return nonNull(points) && points.stream()
                .anyMatch(ap -> ap instanceof N2oPageAccessPoint n2oPageAccessPoint
                        && n2oPageAccessPoint.getPage().equals(pageId));
    }

    private boolean hasUrlAccessPoint(List<AccessPoint> points, String url) {
        return nonNull(points) && points.stream()
                .anyMatch(ap -> ap instanceof N2oUrlAccessPoint n2oUrlAccessPoint
                        && n2oUrlAccessPoint.getMatcher().matches(url));
    }

    protected void transfer(PropertiesAware from, PropertiesAware to) {
        if (isNull(from) || isNull(from.getProperties()))
            return;
        Map<String, Object> properties = from.getProperties();
        if (isNull(properties)
                || isNull(properties.get(SECURITY_PROP_NAME))
                || nonNull(to.getProperties())
                && to.getProperties().containsKey(SECURITY_PROP_NAME))
            return;
        if (isNull(to.getProperties())) {
            to.setProperties(new HashMap<>());
        }
        to.getProperties().put(SECURITY_PROP_NAME, properties.get(SECURITY_PROP_NAME));
    }

    protected void merge(PropertiesAware destination, List<? extends PropertiesAware> sources) {
        if (isNull(destination) || isNull(sources) ||
                (nonNull(destination.getProperties()) && nonNull(destination.getProperties().get(SECURITY_PROP_NAME))))
            return;

        List<Map<String, SecurityObject>> list = sources.stream().filter(pa -> nonNull(pa.getProperties()) && pa.getProperties().containsKey(SECURITY_PROP_NAME)
                        && !CollectionUtils.isEmpty(((Security) pa.getProperties().get(SECURITY_PROP_NAME))))
                .flatMap(pa -> ((Security) pa.getProperties().get(SECURITY_PROP_NAME)).stream())
                .toList();
        if (!CollectionUtils.isEmpty(list)) {
            if (isNull(destination.getProperties()))
                destination.setProperties(new HashMap<>());
            destination.getProperties().put(SECURITY_PROP_NAME, new Security(list));
        }
    }

    protected void collectObjectFilters(PropertiesAware compiled, String objectId,
                                        String operationId, SimpleCompiledAccessSchema schema) {
        if (isNull(objectId)) return;
        if (isNull(compiled.getProperties())) {
            compiled.setProperties(new HashMap<>());
        }
        if (isNull(compiled.getProperties().get(SECURITY_FILTERS_PROP_NAME))) {
            compiled.getProperties().put(SECURITY_FILTERS_PROP_NAME, new SecurityFilters());
        }
        SecurityFilters securityFilters = (SecurityFilters) compiled.getProperties().get(SECURITY_FILTERS_PROP_NAME);
        collectFilters(objectId, schema, securityFilters);
        collectRemoveFilters(objectId, operationId, schema, securityFilters);
    }

    private void collectRemoveFilters(String objectId, String operationId, SimpleCompiledAccessSchema schema, SecurityFilters securityFilters) {
        //removeRoleFilters
        if (nonNull(schema.getN2oRoles())) {
            Map<String, Set<String>> removeRoleFilters = new HashMap<>();
            schema.getN2oRoles().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectRemoveFilters(objectId, operationId, removeRoleFilters, r.getAccessPoints(), r.getId()));
            if (!CollectionUtils.isEmpty(removeRoleFilters)) {
                securityFilters.setRemoveRoleFilters(removeRoleFilters);
            }
        }
        //removePermissionFilters
        if (nonNull(schema.getN2oPermissions())) {
            Map<String, Set<String>> removePermissionFilters = new HashMap<>();
            schema.getN2oPermissions().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectRemoveFilters(objectId, operationId, removePermissionFilters, r.getAccessPoints(), r.getId()));
            if (!CollectionUtils.isEmpty(removePermissionFilters)) {
                securityFilters.setRemovePermissionFilters(removePermissionFilters);
            }
        }
        //removeUserFilters
        if (nonNull(schema.getN2oUserAccesses())) {
            Map<String, Set<String>> removeUserFilters = new HashMap<>();
            schema.getN2oUserAccesses().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectRemoveFilters(objectId, operationId, removeUserFilters, r.getAccessPoints(), r.getId()));
            if (!CollectionUtils.isEmpty(removeUserFilters)) {
                securityFilters.setRemoveUserFilters(removeUserFilters);
            }
        }
        //removeAuthenticatedFilters
        if (nonNull(schema.getAuthenticatedPoints())) {
            securityFilters.setRemoveAuthenticatedFilters(collectRemoveFilters(objectId, operationId, schema.getAuthenticatedPoints()));
        }
        //removeAnonymousFilters
        if (nonNull(schema.getAnonymousPoints())) {
            securityFilters.setRemoveAnonymousFilters(collectRemoveFilters(objectId, operationId, schema.getAnonymousPoints()));
        }
        //removePermitAllFilters
        if (nonNull(schema.getPermitAllPoints())) {
            securityFilters.setRemovePermitAllFilters(collectRemoveFilters(objectId, operationId, schema.getPermitAllPoints()));
        }
    }

    private Set<String> collectRemoveFilters(String objectId, String operationId, List<AccessPoint> accessPoints) {
        return isNull(accessPoints) ? null : accessPoints.stream()
                .filter(ap -> checkByObjectAndOperation(objectId, operationId, ap))
                .flatMap(ap -> Stream.of(((N2oObjectAccessPoint) ap).getRemoveFilters()))
                .collect(Collectors.toSet());
    }

    private void collectRemoveFilters(String objectId, String operationId, Map<String, Set<String>> removePermissionFilters,
                                      AccessPoint[] accessPoints, String id) {
        if (isNull(accessPoints)) return;
        Set<String> rf = new HashSet<>();
        for (AccessPoint ap : accessPoints) {
            if (checkByObjectAndOperation(objectId, operationId, ap)) {
                rf.addAll(Arrays.asList(((N2oObjectAccessPoint) ap).getRemoveFilters()));
            }
        }
        if (!CollectionUtils.isEmpty(rf)) {
            removePermissionFilters.put(id, rf);
        }
    }

    private void collectFilters(String objectId, SimpleCompiledAccessSchema schema, SecurityFilters securityFilters) {
        //roleFilters
        if (nonNull(schema.getN2oRoles())) {
            Map<String, List<N2oObjectFilter>> roleFilters = new HashMap<>();
            schema.getN2oRoles().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectFiltersFromAccessPoints(objectId, roleFilters, r.getAccessPoints(), r.getId()));
            if (!CollectionUtils.isEmpty(roleFilters)) {
                securityFilters.setRoleFilters(roleFilters);
            }
        }
        //permissionFilters
        if (nonNull(schema.getN2oPermissions())) {
            Map<String, List<N2oObjectFilter>> permissionFilters = new HashMap<>();
            schema.getN2oPermissions().stream().filter(p -> nonNull(p.getAccessPoints()))
                    .forEach(p -> collectFiltersFromAccessPoints(objectId, permissionFilters, p.getAccessPoints(), p.getId()));
            if (!CollectionUtils.isEmpty(permissionFilters)) {
                securityFilters.setPermissionFilters(permissionFilters);
            }
        }
        //userFilters
        if (nonNull(schema.getN2oUserAccesses())) {
            Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
            schema.getN2oUserAccesses().stream().filter(u -> nonNull(u.getAccessPoints()))
                    .forEach(u -> collectFiltersFromAccessPoints(objectId, userFilters, u.getAccessPoints(), u.getId()));
            if (!CollectionUtils.isEmpty(userFilters)) {
                securityFilters.setUserFilters(userFilters);
            }
        }
        //authenticatedFilters
        if (nonNull(schema.getAuthenticatedPoints())) {
            securityFilters.setAuthenticatedFilters(collectFiltersFromAccessPointList(objectId, schema.getAuthenticatedPoints()));
        }
        //anonymousFilters
        if (nonNull(schema.getAnonymousPoints())) {
            securityFilters.setAnonymousFilters(collectFiltersFromAccessPointList(objectId, schema.getAnonymousPoints()));
        }
        //permitAllFilters
        if (nonNull(schema.getPermitAllPoints())) {
            securityFilters.setPermitAllFilters(collectFiltersFromAccessPointList(objectId, schema.getPermitAllPoints()));
        }
    }

    private List<N2oObjectFilter> collectFiltersFromAccessPointList(String objectId, List<AccessPoint> accessPoints) {
        return isNull(accessPoints) ? null : accessPoints.stream()
                .filter(ap -> checkByObject(objectId, ap))
                .flatMap(ap -> Stream.of(((N2oObjectFiltersAccessPoint) ap).getFilters()))
                .toList();
    }

    private void collectFiltersFromAccessPoints(String objectId, Map<String, List<N2oObjectFilter>> filters, AccessPoint[] accessPoints, String id) {
        for (AccessPoint ap : accessPoints) {
            if (checkByObject(objectId, ap)) {
                filters.put(id, Arrays.asList(((N2oObjectFiltersAccessPoint) ap).getFilters()));
            }
        }
    }

    private boolean checkByObject(String objectId, AccessPoint ap) {
        return ap instanceof N2oObjectFiltersAccessPoint n2oObjectFiltersAccessPoint
                && StringUtils.maskMatch(n2oObjectFiltersAccessPoint.getObjectId(), objectId)
                && nonNull(n2oObjectFiltersAccessPoint.getFilters());
    }

    private boolean checkByObjectAndOperation(String objectId, String operationId, AccessPoint ap) {
        return ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId))
                && nonNull(n2oObjectAccessPoint.getRemoveFilters());
    }
}