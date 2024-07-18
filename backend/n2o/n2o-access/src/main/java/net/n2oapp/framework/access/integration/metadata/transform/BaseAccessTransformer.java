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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;
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

    private static String DEFAULT_OBJECT_ACCESS_DENIED = "n2o.access.deny_objects";
    private static String DEFAULT_PAGE_ACCESS_DENIED = "n2o.access.deny_pages";
    private static String DEFAULT_URL_ACCESS_DENIED = "n2o.access.deny_urls";

    protected void collectObjectAccess(PropertiesAware compiled, String objectId,
                                       String operationId, SimpleCompiledAccessSchema schema,
                                       CompileProcessor p) {
        if (isNull(objectId)) return;
        Security security = getSecurity(compiled);
        SecurityObject securityObject = new SecurityObject();
        if (CollectionUtils.isNotEmpty(security) &&
                (nonNull(security.get(0).get("object"))
                        || nonNull(security.get(0).get("custom")))) return;

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                            && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (CollectionUtils.isNotEmpty(list)) {
                                    securityObject.setPermitAll(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAuthenticatedPoints())) {
            schema.getAuthenticatedPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                            && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (CollectionUtils.isNotEmpty(list)) {
                                    securityObject.setAuthenticated(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAnonymousPoints())) {
            schema.getAnonymousPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                            && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId)))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (CollectionUtils.isNotEmpty(list)) {
                                    securityObject.setAnonymous(true);
                                }
                                return list;
                            }
                    ));
        }

        List<N2oUserAccess> userAccesses = PermissionAndRoleCollector.collectUsers(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (CollectionUtils.isNotEmpty(userAccesses)) {
            securityObject.setUsernames(userAccesses
                    .stream()
                    .map(N2oUserAccess::getId)
                    .collect(Collectors.toSet()));
        }
        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (CollectionUtils.isNotEmpty(permissions)) {
            securityObject.setPermissions(permissions
                    .stream()
                    .map(N2oPermission::getId)
                    .collect(Collectors.toSet()));
        }
        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(N2oObjectAccessPoint.class,
                OBJECT_ACCESS.apply(objectId, operationId), schema);
        if (CollectionUtils.isNotEmpty(roles)) {
            securityObject.setRoles(roles
                    .stream()
                    .map(N2oRole::getId)
                    .collect(Collectors.toSet()));
        }

        if (securityObject.isEmpty()) {
            Boolean defaultObjectAccessDenied = p.resolve(property(DEFAULT_OBJECT_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultObjectAccessDenied);
            securityObject.setDenied(defaultObjectAccessDenied);
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.get(0).put("object", securityObject);
    }

    protected void collectPageAccess(PropertiesAware compiled, String pageId, SimpleCompiledAccessSchema schema,
                                     CompileProcessor p) {
        if (isNull(pageId))
            return;
        final String originPageId = pageId.split("\\?")[0];
        Security security = getSecurity(compiled);
        if (CollectionUtils.isNotEmpty(security) &&
                (nonNull(security.get(0).get("page"))
                        || nonNull(security.get(0).get("custom")))) return;

        SecurityObject securityObject = new SecurityObject();

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint n2oPageAccessPoint
                            && n2oPageAccessPoint.getPage().equals(originPageId))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setPermitAll(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAuthenticatedPoints())) {
            schema.getAuthenticatedPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint n2oPageAccessPoint
                            && n2oPageAccessPoint.getPage().equals(originPageId))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAuthenticated(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAnonymousPoints())) {
            schema.getAnonymousPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint n2oPageAccessPoint
                            && n2oPageAccessPoint.getPage().equals(originPageId))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAnonymous(true);
                                }
                                return list;
                            }
                    ));
        }

        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(originPageId), schema);
        if (CollectionUtils.isNotEmpty(roles)) {
            securityObject.setRoles(
                    roles
                            .stream()
                            .map(N2oRole::getId)
                            .collect(Collectors.toSet())
            );
        }

        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(originPageId), schema);
        if (CollectionUtils.isNotEmpty(permissions)) {
            securityObject.setPermissions(
                    permissions
                            .stream()
                            .map(N2oPermission::getId)
                            .collect(Collectors.toSet())
            );
        }

        List<N2oUserAccess> userAccesses = PermissionAndRoleCollector.collectUsers(N2oPageAccessPoint.class,
                PAGE_ACCESS.apply(originPageId), schema);
        if (CollectionUtils.isNotEmpty(userAccesses)) {
            securityObject.setUsernames(
                    userAccesses
                            .stream()
                            .map(N2oUserAccess::getId)
                            .collect(Collectors.toSet())
            );
        }

        if (securityObject.isEmpty()) {
            Boolean defaultPageAccessDenied = p.resolve(property(DEFAULT_PAGE_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultPageAccessDenied);
            securityObject.setDenied(defaultPageAccessDenied);
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.get(0).put("page", securityObject);
    }

    protected void collectUrlAccess(PropertiesAware compiled, String url, SimpleCompiledAccessSchema schema,
                                    CompileProcessor p) {
        if (isNull(url))
            return;
        Security security = getSecurity(compiled);
        if (CollectionUtils.isNotEmpty(security) &&
                (nonNull(security.get(0).get("url"))
                        || nonNull(security.get(0).get("custom")))) return;

        SecurityObject securityObject = new SecurityObject();

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oUrlAccessPoint n2oUrlAccessPoint
                            && n2oUrlAccessPoint.getMatcher().matches(url))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setPermitAll(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAuthenticatedPoints())) {
            schema.getAuthenticatedPoints().stream()
                    .filter(ap -> ap instanceof N2oUrlAccessPoint n2oUrlAccessPoint
                            && n2oUrlAccessPoint.getMatcher().matches(url))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAuthenticated(true);
                                }
                                return list;
                            }
                    ));
        }

        if (nonNull(schema.getAnonymousPoints())) {
            schema.getAnonymousPoints().stream()
                    .filter(ap -> ap instanceof N2oUrlAccessPoint n2oUrlAccessPoint
                            && n2oUrlAccessPoint.getMatcher().matches(url))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toList(),
                            list -> {
                                if (list.size() == 1) {
                                    securityObject.setAnonymous(true);
                                }
                                return list;
                            }
                    ));
        }

        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(N2oUrlAccessPoint.class,
                URL_ACCESS.apply(url), schema);
        if (CollectionUtils.isNotEmpty(roles)) {
            securityObject.setRoles(
                    roles
                            .stream()
                            .map(N2oRole::getId)
                            .collect(Collectors.toSet())
            );
        }

        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(N2oUrlAccessPoint.class,
                URL_ACCESS.apply(url), schema);
        if (CollectionUtils.isNotEmpty(permissions)) {
            securityObject.setPermissions(
                    permissions
                            .stream()
                            .map(N2oPermission::getId)
                            .collect(Collectors.toSet())
            );
        }

        List<N2oUserAccess> userAccesses = PermissionAndRoleCollector.collectUsers(N2oUrlAccessPoint.class,
                URL_ACCESS.apply(url), schema);
        if (CollectionUtils.isNotEmpty(userAccesses)) {
            securityObject.setUsernames(
                    userAccesses
                            .stream()
                            .map(N2oUserAccess::getId)
                            .collect(Collectors.toSet())
            );
        }

        if (securityObject.isEmpty()) {
            Boolean defaultUrlAccessDenied = p.resolve(property(DEFAULT_URL_ACCESS_DENIED), Boolean.class);
            securityObject.setPermitAll(!defaultUrlAccessDenied);
            securityObject.setDenied(defaultUrlAccessDenied);
        }
        if (security.isEmpty())
            security.add(new HashMap<>());
        security.get(0).put("url", securityObject);
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
                        && CollectionUtils.isNotEmpty(((Security) pa.getProperties().get(SECURITY_PROP_NAME))))
                .flatMap(pa -> ((Security) pa.getProperties().get(SECURITY_PROP_NAME)).stream())
                .toList();
        if (CollectionUtils.isNotEmpty(list)) {
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
            if (MapUtils.isNotEmpty(removeRoleFilters)) {
                securityFilters.setRemoveRoleFilters(removeRoleFilters);
            }
        }
        //removePermissionFilters
        if (nonNull(schema.getN2oPermissions())) {
            Map<String, Set<String>> removePermissionFilters = new HashMap<>();
            schema.getN2oPermissions().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectRemoveFilters(objectId, operationId, removePermissionFilters, r.getAccessPoints(), r.getId()));
            if (MapUtils.isNotEmpty(removePermissionFilters)) {
                securityFilters.setRemovePermissionFilters(removePermissionFilters);
            }
        }
        //removeUserFilters
        if (nonNull(schema.getN2oUserAccesses())) {
            Map<String, Set<String>> removeUserFilters = new HashMap<>();
            schema.getN2oUserAccesses().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectRemoveFilters(objectId, operationId, removeUserFilters, r.getAccessPoints(), r.getId()));
            if (MapUtils.isNotEmpty(removeUserFilters)) {
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
        if (CollectionUtils.isNotEmpty(rf)) {
            removePermissionFilters.put(id, rf);
        }
    }

    private void collectFilters(String objectId, SimpleCompiledAccessSchema schema, SecurityFilters securityFilters) {
        //roleFilters
        if (nonNull(schema.getN2oRoles())) {
            Map<String, List<N2oObjectFilter>> roleFilters = new HashMap<>();
            schema.getN2oRoles().stream().filter(r -> nonNull(r.getAccessPoints()))
                    .forEach(r -> collectFiltersFromAccessPoints(objectId, roleFilters, r.getAccessPoints(), r.getId()));
            if (MapUtils.isNotEmpty(roleFilters)) {
                securityFilters.setRoleFilters(roleFilters);
            }
        }
        //permissionFilters
        if (nonNull(schema.getN2oPermissions())) {
            Map<String, List<N2oObjectFilter>> permissionFilters = new HashMap<>();
            schema.getN2oPermissions().stream().filter(p -> nonNull(p.getAccessPoints()))
                    .forEach(p -> collectFiltersFromAccessPoints(objectId, permissionFilters, p.getAccessPoints(), p.getId()));
            if (MapUtils.isNotEmpty(permissionFilters)) {
                securityFilters.setPermissionFilters(permissionFilters);
            }
        }
        //userFilters
        if (nonNull(schema.getN2oUserAccesses())) {
            Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
            schema.getN2oUserAccesses().stream().filter(u -> nonNull(u.getAccessPoints()))
                    .forEach(u -> collectFiltersFromAccessPoints(objectId, userFilters, u.getAccessPoints(), u.getId()));
            if (MapUtils.isNotEmpty(userFilters)) {
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
        return ap instanceof N2oObjectFiltersAccessPoint
                && StringUtils.maskMatch(((N2oObjectFiltersAccessPoint) ap).getObjectId(), objectId)
                && nonNull(((N2oObjectFiltersAccessPoint) ap).getFilters());
    }

    private boolean checkByObjectAndOperation(String objectId, String operationId, AccessPoint ap) {
        return ap instanceof N2oObjectAccessPoint n2oObjectAccessPoint
                && StringUtils.maskMatch(n2oObjectAccessPoint.getObjectId(), objectId)
                && (isNull(operationId) || StringUtils.maskMatch(n2oObjectAccessPoint.getAction(), operationId))
                && nonNull(n2oObjectAccessPoint.getRemoveFilters());
    }
}
