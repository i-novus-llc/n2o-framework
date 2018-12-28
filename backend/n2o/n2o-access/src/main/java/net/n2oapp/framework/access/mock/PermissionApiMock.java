package net.n2oapp.framework.access.mock;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.access.simple.PermissionApi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author V. Alexeev.
 */
public class PermissionApiMock implements PermissionApi {

    public List<String> permission = new ArrayList<>();
    public List<String> roles = new ArrayList<>();
    public List<String> users = new ArrayList<>();

    private List<String> allowedPermissions = new ArrayList<>();
    private List<String> allowedRoles = new ArrayList<>();

    @Override
    public boolean hasPermission(UserContext user, String permissionId) {
        permission.add(permissionId);
        return allowedPermissions.contains(permissionId);
    }

    @Override
    public boolean hasRole(UserContext user, String roleId) {
        roles.add(roleId);
        return allowedRoles.contains(roleId);
    }

    @Override
    public boolean hasAuthentication(UserContext user) {
        return false;
    }

    @Override
    public boolean hasUsername(UserContext user, String name) {
        users.add(name);
        return false;
    }

    public void addAllowedPermission(String allowedPermission) {
        this.allowedPermissions.add(allowedPermission);
    }

    public void addAllowedRole(String allowedRole) {
        this.allowedRoles.add(allowedRole);
    }

    public void clearAllowedPermissions() {
        this.allowedPermissions.clear();
    }

    public void clearCalls() {
        this.roles.clear();
        this.permission.clear();
        this.users.clear();
    }

}
