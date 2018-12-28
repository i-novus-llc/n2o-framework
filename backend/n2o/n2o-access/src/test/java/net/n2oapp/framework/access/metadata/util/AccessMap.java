package net.n2oapp.framework.access.metadata.util;

import net.n2oapp.framework.access.api.model.ObjectPermission;
import net.n2oapp.framework.access.api.model.Permission;

import java.util.HashMap;
import java.util.Map;

/**
 * @author operehod
 * @since 13.04.2015
 */
public class AccessMap {

    private Map<String, Permission> map = new HashMap<>();

    public void addPermissionForObject(String objectId, String action, ObjectPermission permission) {
        map.put("objectAccess." + objectId + "." + action, permission);
    }

    public ObjectPermission getPermissionForObject(String objectId, String action) {
        ObjectPermission permission = (ObjectPermission) map.get("objectAccess." + objectId + "." + action);
        if (permission == null)
            permission = new ObjectPermission(false, objectId);
        return permission;
    }

    public void addReferencePermissionForObject(String objectId, ObjectPermission permission) {
        map.put("refAccess." + objectId, permission);
    }

    public ObjectPermission getReferencePermissionForObject(String objectId) {
        ObjectPermission permission = (ObjectPermission) map.get("refAccess." + objectId);
        if (permission == null)
            permission = new ObjectPermission(false, objectId);
        return permission;
    }


    public Permission getPermissionForModule(String moduleId) {
        Permission permission = map.get("moduleAccess." + moduleId);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForModule(String moduleId, Permission permission) {
        map.put("moduleAccess." + moduleId, permission);
    }


    public Permission getPermissionForPageWithinModule(String moduleId, String pageId) {
        Permission permission = map.get("pageWithinModuleAccess." + moduleId + "." + pageId);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForPageWithinModule(String moduleId, String pageId, Permission permission) {
        map.put("pageWithinModuleAccess." + moduleId + "." + pageId, permission);
    }


    public Permission getPermissionForPage(String pageId) {
        Permission permission = map.get("pageAccess." + pageId);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForPage(String pageId, Permission permission) {
        map.put("pageAccess." + pageId, permission);
    }


    public Permission getPermissionForContainer(String pageId, String containerId) {
        Permission permission = map.get("containerAccess." + pageId + "." + containerId);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForContainer(String pageId, String containerId, Permission permission) {
        map.put("containerAccess." + pageId + "." + containerId, permission);
    }


    public Permission getPermissionForMenuItem(String pageId, String containerId, String menuItemId) {
        Permission permission = map.get("menuAccess." + pageId + "." + containerId + "." + menuItemId);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForMenuItem(String pageId, String containerId, String menuItemId, Permission permission) {
        map.put("menuAccess." + pageId + "." + containerId + "." + menuItemId, permission);
    }

    public Permission getPermissionForUrlPattern(String pattern) {
        Permission permission = map.get("urlAccess." + pattern);
        if (permission == null)
            permission = new Permission(false);
        return permission;
    }

    public void addPermissionForUrlPattern(String pattern, Permission permission) {
        map.put("urlAccess." + pattern, permission);
    }

    public Permission getPermissionForColumn(String pageId, String containerId, String columnId) {
        String key = String.format("columnAccess.%s.%s.%s", pageId, containerId, columnId);
        return map.getOrDefault(key, new Permission(false));
    }

    public void addPermissionForColumn(String pageId, String containerId, String columnId, Permission permission) {
        map.put(String.format("columnAccess.%s.%s.%s", pageId, containerId, columnId), permission);
    }

    public Permission getPermissionForFilter(String queryId, String filterId) {
        String key = String.format("filterAccess.%s.%s", queryId, filterId);
        return map.getOrDefault(key, new Permission(false));
    }

    public void addPermissionForFilter(String queryId, String filterId, Permission permission) {
        map.put(String.format("filterAccess.%s.%s", queryId, filterId), permission);
    }
}
