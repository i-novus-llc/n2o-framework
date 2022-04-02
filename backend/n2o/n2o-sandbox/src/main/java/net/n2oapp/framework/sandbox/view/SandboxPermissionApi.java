package net.n2oapp.framework.sandbox.view;

import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.user.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SandboxPermissionApi implements PermissionApi {

    private ContextProcessor contextProcessor;

    @Override
    public boolean hasPermission(UserContext userContext, String permission) {
        return ((List<String>)userContext.get("permissions")).contains(permission);
    }

    @Override
    public boolean hasRole(UserContext userContext, String roles) {
        return ((List<String>)userContext.get("roles")).contains(roles);
    }

    @Override
    public boolean hasAuthentication(UserContext userContext) {
        return userContext.get("username") != null;
    }

    @Override
    public boolean hasUsername(UserContext userContext, String username) {
        return userContext.get("username").equals(username);
    }
}
