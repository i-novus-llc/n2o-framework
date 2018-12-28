package net.n2oapp.framework.access.simple;

import net.n2oapp.framework.access.AdminService;
import net.n2oapp.framework.access.api.AuthorizationApi;
import net.n2oapp.framework.access.api.model.ObjectPermission;
import net.n2oapp.framework.access.api.model.Permission;
import net.n2oapp.framework.access.api.model.filter.N2oAccessFilter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.*;
import net.n2oapp.framework.access.metadata.accesspoint.util.FilterMerger;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.user.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Сборка прав из simple схемы доступа
 */
@SuppressWarnings("unchecked")
public class SimpleAuthorizationApi implements AuthorizationApi {

    private final static String OBJECT_NULL_MSG = "objectId is null";
    private Boolean defaultObjectAccess;
    private Boolean defaultReferenceAccess;
    private Boolean defaultPageAccess;
    private Boolean defaultUrlAccess;
    private Boolean defaultColumnAccess;
    private Boolean defaultFilterAccess;
    private PermissionApi permissionApi;
    private AdminService adminService;
    private SimpleCompiledAccessSchema schema;
    private ReadCompileBindTerminalPipeline pipeline;
    private String accessSchemaId;

    public SimpleAuthorizationApi(PermissionApi permissionApi, AdminService adminService, ReadCompileBindTerminalPipeline pipeline, String accessSchemaId,
                                  Boolean defaultObjectAccess, Boolean defaultReferenceAccess, Boolean defaultPageAccess,
                                  Boolean defaultUrlAccess, Boolean defaultColumnAccess, Boolean defaultFilterAccess) {
        this.defaultObjectAccess = defaultObjectAccess;
        this.defaultReferenceAccess = defaultReferenceAccess;
        this.defaultPageAccess = defaultPageAccess;
        this.defaultUrlAccess = defaultUrlAccess;
        this.defaultColumnAccess = defaultColumnAccess;
        this.defaultFilterAccess = defaultFilterAccess;
        this.permissionApi = permissionApi;
        this.adminService = adminService;
        this.pipeline = pipeline;
        this.accessSchemaId = accessSchemaId;
    }

    @Override
    public ObjectPermission getPermissionForObject(UserContext user, String objectId, String action) {
        if (objectId == null) {
            return ObjectPermission.allowed(OBJECT_NULL_MSG);
        }

        ObjectPermission permission = getPermission(N2oObjectAccessPoint.class,
                PermissionAndRoleCollector.OBJECT_ACCESS.apply(objectId, action), user, ac -> new ObjectPermission(ac, objectId),
                defaultObjectAccess);
        collectFilters(user, permission, objectId, action);
        return permission;
    }

    @Override
    public ObjectPermission getPermissionForObject(UserContext user, String objectId) {
        return getPermissionForObject(user, objectId, "read");
    }

    @Override
    public ObjectPermission getReferencePermissionForObject(UserContext user, String objectId) {
        if (objectId == null) {
            return ObjectPermission.allowed(OBJECT_NULL_MSG);
        }
        ObjectPermission permission = getPermission(N2oReferenceAccessPoint.class,
                PermissionAndRoleCollector.REFERENCE_ACCESS.apply(objectId), user, ac -> new ObjectPermission(ac, objectId),
                defaultReferenceAccess);
        collectFilters(user, permission, objectId, null);
        return permission;
    }

    @Override
    public Permission getPermissionForPage(UserContext user, String pageId) {
        return getPermission(N2oPageAccessPoint.class,
                PermissionAndRoleCollector.PAGE_ACCESS.apply(pageId), user, Permission::new, defaultPageAccess);
    }

    @Override
    public Permission getPermissionForUrlPattern(UserContext user, String pattern) {
        return getPermission(N2oUrlAccessPoint.class,
                PermissionAndRoleCollector.URL_ACCESS.apply(pattern), user, Permission::new, defaultUrlAccess);
    }

    @Override
    public Permission getPermissionForColumn(UserContext userContext, String pageId, String containerId, String columnId) {
        return getPermission(N2oColumnAccessPoint.class,
                PermissionAndRoleCollector.COLUMN_ACCESS.apply(pageId, containerId, columnId), userContext, Permission::new, defaultColumnAccess);
    }

    @Override
    public Permission getPermissionForFilter(UserContext userContext, String queryId, String filterId) {
        return getPermission(N2oFilterAccessPoint.class,
                PermissionAndRoleCollector.FILTER_ACCESS.apply(queryId, filterId), userContext, Permission::new, defaultFilterAccess);
    }


    protected boolean checkAccess(List<N2oPermission> p, List<N2oRole> r, List<N2oUserAccess> s, UserContext u, Boolean defaultProperty) {
        if (p.isEmpty() && r.isEmpty() && s.isEmpty()) {
            return defaultProperty;
        }
        return p.stream().anyMatch(p1 -> permissionApi.hasPermission(u, p1.getId()))
                || r.stream().anyMatch(r1 -> permissionApi.hasRole(u, r1.getId()))
                || s.stream().anyMatch(s1 -> permissionApi.hasUsername(u, s1.getId()));
    }

    protected <T extends AccessPoint> boolean checkAccess(Class<T> type, Predicate<T> checker, UserContext u, Boolean defaultProperty) {
        List<N2oRole> roles = PermissionAndRoleCollector.collectRoles(type, checker, schema);
        List<N2oPermission> permissions = PermissionAndRoleCollector.collectPermission(type, checker, schema);
        List<N2oUserAccess> users = PermissionAndRoleCollector.collectUsers(type, checker, schema);

        return checkAccess(permissions, roles, users, u, defaultProperty);
    }

    protected <T extends AccessPoint> boolean checkPermitAllAccess(Class<T> type, Predicate<T> checker) {
        List<AccessPoint> permitAllPoints = getSchema().getPermitAllPoints();
        return permitAllPoints != null && !permitAllPoints.isEmpty() &&
                permitAllPoints.stream().filter(type::isInstance).map(type::cast).filter(checker).findFirst().isPresent();
    }

    protected <T extends AccessPoint> boolean checkAnonymousAccess(Class<T> type, Predicate<T> checker) {
        List<AccessPoint> anonymousPoints = getSchema().getAnonymousPoints();
        return anonymousPoints != null && !anonymousPoints.isEmpty() &&
                anonymousPoints.stream().filter(type::isInstance).map(type::cast).filter(checker).findFirst().isPresent();
    }

    protected <T extends AccessPoint> boolean checkAuthenticatedAccess(Class<T> type, Predicate<T> checker, UserContext user) {
        List<AccessPoint> authenticatedPoints = getSchema().getAuthenticatedPoints();
        return authenticatedPoints != null && !authenticatedPoints.isEmpty() &&
                authenticatedPoints.stream().filter(type::isInstance).map(type::cast).filter(checker).findFirst().isPresent()
                && permissionApi.hasAuthentication(user);
    }

    protected <T extends AccessPoint, R extends Permission> R getPermission(Class<T> type, Predicate<T> checker, UserContext u,
                                                                            Function<Boolean, R> factory, Boolean defaultProperty) {
        if (adminService.isAdmin(u.getUsername())) {
            return factory.apply(true);
        }

        if (checkPermitAllAccess(type, checker)) {
            return factory.apply(true);
        }

        if (checkAnonymousAccess(type, checker)) {
            return factory.apply(true);
        }

        if (checkAuthenticatedAccess(type, checker, u)) {
            return factory.apply(true);
        }

        return factory.apply(checkAccess(type, checker, u, defaultProperty));
    }

    /**
     * Сборка фильтров по объекту и действию
     * @param user пользователь
     * @param permission право на объект
     * @param objectId объект
     * @param actionId действие
     */
    private void collectFilters(UserContext user, ObjectPermission permission, String objectId, String actionId) {
        if (adminService.isAdmin(user.getUsername())) {
            return;
        }
        List<N2oAccessFilter> filters = PermissionAndRoleCollector.collectFilters(r -> permissionApi.hasRole(user, r.getId()),
                p -> permissionApi.hasPermission(user, p.getId()), u -> permissionApi.hasUsername(user, u.getId()),
                objectId, actionId, getSchema());
        permission.setAccessFilters(FilterMerger.merge(resolveContext(user, filters)));
    }

    /**
     * Разрешает контекст пользователя и удаляет фильтры, если контекст вернул null
     * @param user конектст
     * @param filters фильтры
     * @return фильтры с разрешенным контекстом
     */
    private List<N2oAccessFilter> resolveContext(UserContext user, List<N2oAccessFilter> filters) {
        ContextProcessor contextProcessor = new ContextProcessor(user);
        return filters.stream().map(filter -> {
            switch (filter.getType().arity) {
                case nullary:
                    filter.setValue(String.valueOf(true));
                    break;
                case unary: {
                    Object resolve = contextProcessor.resolve(filter.getValue());
                    filter.setValue(resolve != null ? resolve.toString() : null);
                }
                break;
                case n_ary: {
                    if (filter.getValue() != null) {
                        Object resolve = contextProcessor.resolve(filter.getValue());
                        if (resolve != null) {
                            if (!(resolve instanceof Collection))
                                throw new IllegalStateException("Context value [" + filter.getValue() + "] must be Collection");
                            Collection<Object> list = (Collection) resolve;
                            filter.setValues(list.stream().map(Object::toString).collect(Collectors.toList()));
                            filter.setValue(null);
                        } else {
                            filter.setValues(null);
                            filter.setValue(null);
                        }
                    } else {
                        List<String> resolves = new ArrayList<>();
                        for (String value : filter.getValues()) {
                            Object resolve = contextProcessor.resolve(value);
                            if (resolve != null)
                                resolves.add(resolve.toString());
                        }
                        filter.setValues(resolves);
                    }
                }
                break;
            }
            return filter;
        }).filter(f -> f.getValue() != null || (f.getValues() != null)).collect(Collectors.toList());
    }

    public SimpleCompiledAccessSchema getSchema() {
        if (schema == null) {
            schema = (SimpleCompiledAccessSchema) pipeline.get(new AccessContext(accessSchemaId), null);
        }
        return schema;
    }
}
