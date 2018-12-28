package net.n2oapp.framework.access.api.chained;

import net.n2oapp.framework.access.api.AuthorizationApi;
import net.n2oapp.framework.access.api.model.ComplexPermission;
import net.n2oapp.framework.access.api.model.ObjectPermission;
import net.n2oapp.framework.access.api.model.Permission;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.user.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Прокси вызовов API авторизации
 */
public class AuthorizationApiCaller implements AuthorizationApi {

    private AuthorizationApi authorizationApi;
    private List<Permission> permissions = new ArrayList<>();

    private Map<String, ObjectPermission> permissionForObject = new HashMap<>();
    private Map<String, ObjectPermission> referencePermissionForObject = new HashMap<>();
    private Map<String, Permission> permissionForPage = new HashMap<>();
    private Map<String, Permission> permissionForUrlPattern = new HashMap<>();
    private Map<String, Permission> permissionForColumn = new HashMap<>();
    private Map<String, Permission> permissionForFilter = new HashMap<>();


    public AuthorizationApiCaller(AuthorizationApi authorizationApi) {
        this.authorizationApi = authorizationApi;
    }

    /**
     * вызыается когда известно что caller уже содержит один permissionForObject в контексте
     */
    public ObjectPermission getPermissionForObject() {
        if (permissionForObject.size() != 1)
            throw new N2oException("Expected only one permission for object in context. Actual number:{0}").addData(permissionForObject.size());
        return permissionForObject.values().iterator().next();
    }


    @Override
    public ObjectPermission getPermissionForObject(UserContext user, String objectId, String action) {
        String key = objectId + action;
        ObjectPermission res = permissionForObject.get(key);
        if (res == null) {
            res = authorizationApi.getPermissionForObject(user, objectId, action);
            permissionForObject.put(key, res);
        }
        return res;
    }

    @Override
    public ObjectPermission getReferencePermissionForObject(UserContext user, String objectId) {
        ObjectPermission res = referencePermissionForObject.get(objectId);
        if (res == null) {
            res = authorizationApi.getReferencePermissionForObject(user, objectId);
            referencePermissionForObject.put(objectId, res);
        }
        return res;
    }

    @Override
    public Permission getPermissionForPage(UserContext user, String pageId) {
        Permission res = permissionForPage.get(pageId);
        if (res == null) {
            res = authorizationApi.getPermissionForPage(user, pageId);
            permissionForPage.put(pageId, res);
        }
        return res;
    }

    @Override
    public Permission getPermissionForUrlPattern(UserContext user, String pattern) {
        Permission res = permissionForUrlPattern.get(pattern);
        if (res == null) {
            res = authorizationApi.getPermissionForUrlPattern(user, pattern);
            permissionForUrlPattern.put(pattern, res);
        }
        return res;
    }

    @Override
    public Permission getPermissionForColumn(UserContext userContext, String pageId, String containerId, String columnId) {
        String key = pageId.concat(containerId).concat(columnId);
        Permission res = permissionForColumn.get(key);
        if (res == null) {
            res = authorizationApi.getPermissionForColumn(userContext, pageId, containerId, columnId);
            permissionForColumn.put(key, res);
        }
        return res;
    }

    @Override
    public Permission getPermissionForFilter(UserContext userContext, String queryId, String filterId) {
        String key = queryId.concat(filterId);
        Permission res = permissionForFilter.get(key);
        if (res == null) {
            res = authorizationApi.getPermissionForFilter(userContext, queryId, filterId);
            permissionForFilter.put(key, res);
        }
        return res;
    }

    /**
     * Мердж нескольких пермишенов в контексте caller. Алгоритм:
     * 1. Пытаемся смержить средствами AuthorizationApi, которое используем как провайдер
     * 2. Если провайдер не умеет мержить, то используем ComplexPermission
     */
    public Permission resolveToPermission() {
        if (permissions.isEmpty())
            return Permission.allowed();
        return new ComplexPermission(permissions);
    }


    public AuthorizationApiCaller considerPermissionForObject(UserContext user, String objectId, String action) {
        this.permissions.add(getPermissionForObject(user, objectId, action));
        return this;
    }

    public AuthorizationApiCaller considerPermissionForObject(UserContext user, String objectId) {
        return considerPermissionForObject(user, objectId, "read");
    }

    public AuthorizationApiCaller considerReferencePermissionForObject(UserContext user, String objectId) {
        this.permissions.add(getReferencePermissionForObject(user, objectId));
        return this;
    }

    public AuthorizationApiCaller considerPermissionForUrlPattern(UserContext user, String pattern) {
        this.permissions.add(getPermissionForUrlPattern(user, pattern));
        return this;
    }

    public AuthorizationApiCaller considerPermissionForPage(UserContext user, String pageId) {
        this.permissions.add(getPermissionForPage(user, pageId));
        return this;
    }

    public AuthorizationApiCaller addPermissionForConsider(Permission permission) {
        this.permissions.add(permission);
        return this;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean isEmpty() {
        return permissions.isEmpty();
    }
}
