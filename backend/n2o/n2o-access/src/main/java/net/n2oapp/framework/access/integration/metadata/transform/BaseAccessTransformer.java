package net.n2oapp.framework.access.integration.metadata.transform;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
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
        Security.SecurityObject securityObject = new Security.SecurityObject();
        if (isNull(security.getSecurityMap())) {
            security.setSecurityMap(new HashMap<>());
        } else if (nonNull(security.getSecurityMap().get("object"))
                || nonNull(security.getSecurityMap().get("custom"))) return;

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oObjectAccessPoint
                            && StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getAction(), operationId)))
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
                    .filter(ap -> ap instanceof N2oObjectAccessPoint
                            && StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getAction(), operationId)))
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
                    .filter(ap -> ap instanceof N2oObjectAccessPoint
                            && StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getObjectId(), objectId)
                            && (isNull(operationId) || StringUtils.maskMatch(((N2oObjectAccessPoint) ap).getAction(), operationId)))
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
        security.getSecurityMap().put("object", securityObject);
    }

    protected void collectPageAccess(PropertiesAware compiled, String pageId, SimpleCompiledAccessSchema schema,
                                     CompileProcessor p) {
        if (isNull(pageId))
            return;
        final String originPageId = pageId.split("\\?")[0];
        Security security = getSecurity(compiled);
        if (isNull(security.getSecurityMap())) {
            security.setSecurityMap(new HashMap<>());
        } else if (nonNull(security.getSecurityMap().get("page"))
                || nonNull(security.getSecurityMap().get("custom"))) return;

        Security.SecurityObject securityObject = new Security.SecurityObject();

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oPageAccessPoint
                            && ((N2oPageAccessPoint) ap).getPage().equals(originPageId))
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
                    .filter(ap -> ap instanceof N2oPageAccessPoint
                            && ((N2oPageAccessPoint) ap).getPage().equals(originPageId))
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
                    .filter(ap -> ap instanceof N2oPageAccessPoint
                            && ((N2oPageAccessPoint) ap).getPage().equals(originPageId))
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
        security.getSecurityMap().put("page", securityObject);
    }

    protected void collectUrlAccess(PropertiesAware compiled, String url, SimpleCompiledAccessSchema schema,
                                     CompileProcessor p) {
        if (isNull(url))
            return;
        Security security = getSecurity(compiled);
        if (isNull(security.getSecurityMap())) {
            security.setSecurityMap(new HashMap<>());
        } else if (nonNull(security.getSecurityMap().get("url"))
                || nonNull(security.getSecurityMap().get("custom"))) return;

        Security.SecurityObject securityObject = new Security.SecurityObject();

        if (nonNull(schema.getPermitAllPoints())) {
            schema.getPermitAllPoints().stream()
                    .filter(ap -> ap instanceof N2oUrlAccessPoint
                            && ((N2oUrlAccessPoint) ap).getMatcher().matches(url))
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
                    .filter(ap -> ap instanceof N2oUrlAccessPoint
                            && ((N2oUrlAccessPoint) ap).getMatcher().matches(url))
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
                    .filter(ap -> ap instanceof N2oUrlAccessPoint
                            && ((N2oUrlAccessPoint) ap).getMatcher().matches(url))
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
        security.getSecurityMap().put("url", securityObject);
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
        Map<String, List<Security.SecurityObject>> securityObjects = new HashMap<>();
        for (PropertiesAware source : sources) {
            if (nonNull(source.getProperties()) && source.getProperties().containsKey(SECURITY_PROP_NAME)) {
                Security sourceSecurity = (Security) source.getProperties().get(SECURITY_PROP_NAME);
                if (isNull(sourceSecurity.getSecurityMap()) || sourceSecurity.getSecurityMap().isEmpty())
                    continue;
                for (String securityObjectKey : sourceSecurity.getSecurityMap().keySet()) {
                    if (!securityObjects.containsKey(securityObjectKey)) {
                        securityObjects.put(securityObjectKey, new ArrayList<>());
                    }
                    securityObjects.get(securityObjectKey)
                            .add(sourceSecurity.getSecurityMap().get(securityObjectKey));
                }
            }
        }

        Security security = new Security();
        security.setSecurityMap(new HashMap<>());
        for (Map.Entry<String, List<Security.SecurityObject>> securityEntry : securityObjects.entrySet()) {
            Security.SecurityObject securityObject = new Security.SecurityObject();
            if (securityEntry.getValue().size() == sources.size())
                mergeSecurityObjects(securityObject, securityEntry.getValue());
            if (securityObject.isNotEmpty())
                security.getSecurityMap().put(securityEntry.getKey(), securityObject);
        }
        if (MapUtils.isNotEmpty(security.getSecurityMap())) {
            if (isNull(destination.getProperties()))
                destination.setProperties(new HashMap<>());
            destination.getProperties().put(SECURITY_PROP_NAME, security);
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
                .collect(Collectors.toList());
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
        return ap instanceof N2oObjectAccessPoint
                && StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getObjectId(), objectId)
                && (isNull(operationId) || StringUtils.maskMatch(((N2oObjectAccessPoint)ap).getAction(), operationId))
                && nonNull(((N2oObjectAccessPoint)ap).getRemoveFilters());
    }

    private void mergeSecurityObjects(Security.SecurityObject destination, List<Security.SecurityObject> sources) {
        boolean permitAll = false;
        boolean denied = true;
        boolean anonymous = false;
        boolean authenticated = false;

        for (Security.SecurityObject source : sources) {
            denied = (nonNull(source.getDenied()) && source.getDenied()) && denied;
            permitAll = (nonNull(source.getPermitAll()) && source.getPermitAll()) || permitAll;
            anonymous = (nonNull(source.getAnonymous()) && source.getAnonymous()) || anonymous;
            authenticated = (nonNull(source.getAuthenticated()) && source.getAuthenticated()) || authenticated;

            if (nonNull(source.getUsernames())) {
                if (isNull(destination.getUsernames()))
                    destination.setUsernames(new HashSet<>());
                destination.getUsernames().addAll(source.getUsernames());
            }
            if (nonNull(source.getPermissions())) {
                if (isNull(destination.getPermissions()))
                    destination.setPermissions(new HashSet<>());
                destination.getPermissions().addAll(source.getPermissions());
            }
            if (nonNull(source.getRoles())) {
                if (isNull(destination.getRoles()))
                    destination.setRoles(new HashSet<>());
                destination.getRoles().addAll(source.getRoles());
            }
        }
        if (permitAll) {
            destination.setPermitAll(true);
        } else if (authenticated || anonymous) {
            destination.setAnonymous(anonymous);
            destination.setAuthenticated(authenticated);
        } else if (denied) {
            destination.setDenied(true);
        }
    }
}
