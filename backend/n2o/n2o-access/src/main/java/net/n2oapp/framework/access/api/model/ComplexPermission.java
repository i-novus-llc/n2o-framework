package net.n2oapp.framework.access.api.model;

import java.util.List;
import java.util.Optional;

/**
 * User: operehod
 * Date: 17.02.2015
 * Time: 11:49
 */
public class ComplexPermission extends Permission {

    private List<Permission> permissions;

    public ComplexPermission(List<Permission> permissions) {
        super(resolve(permissions));
        for (Permission permission : permissions) {
            if (!permission.isAllowed()) {
                setDetailedMessage(permission.getDetailedMessage());
                setTechMessage(permission.getTechMessage());
            }
        }
        this.permissions = permissions;
    }

    private static boolean resolve(List<Permission> permissions) {
        for (Permission permission : permissions) {
            if (!permission.isAllowed())
                return false;
        }
        return true;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    @SuppressWarnings("unchecked")
    public <T extends Permission> Optional<T> getPermissionByType(Class<T> type) {
        return (Optional<T>) permissions.stream()
                .map(p -> {
                    Optional<T> res = p instanceof ComplexPermission ? ((ComplexPermission)p).getPermissionByType(type) : Optional.empty();
                    return res.isPresent() ? res.get() : p;
                })
                .filter(p -> p.getClass() == type).findFirst();
    }
}
