package net.n2oapp.framework.access.metadata;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityProviderTest {

    private PermissionApi permissionApi;
    private ActionRequestInfo requestInfo;

    @Before
    public void setUp() {
        permissionApi = mock(PermissionApi.class);
        requestInfo = mock(ActionRequestInfo.class);

    }

    @Test(expected = UnauthorizedException.class)
    public void testDenied() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);
        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
        Assert.fail("Expected exception to be thrown");
    }

    @Test
    public void testPermitAll() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
    }

    @Test
    public void testUserIsNotAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
        securityObject.setAnonymous(false);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    public void testAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
    }

    @Test(expected = AccessDeniedException.class)
    public void testRolesUsernamesPermissionsAreNull() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
        Assert.fail("Expected exception to be thrown");
    }

    @Test
    public void testHasRole() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);

        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(false);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setRoles(Arrays.asList("role2", "role1", "admin"));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        when(permissionApi.hasRole(userContext, "admin")).thenReturn(false);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testHasPermission() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);

        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p2")).thenReturn(false);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setPermissions(Arrays.asList("p0", "p1", "p2"));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testHasUsernames() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);

        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(true);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setUsernames(Arrays.asList("n0", "n1", "n2"));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(false);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testCombinations() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);

        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);

        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r1")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);

        Map<String, Object> properties = new HashMap<>();
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityObject.setUsernames(Arrays.asList("n0", "n1"));
        securityObject.setPermissions(Arrays.asList("p0", "p1"));
        securityObject.setRoles(Arrays.asList("r0", "r1"));
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(true);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        when(permissionApi.hasRole(userContext, "r0")).thenReturn(true);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        securityObject.setUsernames(Arrays.asList("n1"));
        securityObject.setPermissions(Arrays.asList("p0"));
        securityObject.setRoles(Arrays.asList("r1"));
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        securityObject.setRoles(Arrays.asList("r0", "r1"));
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }

        securityObject.setRoles(null);
        securityObject.setPermissions(null);
        securityObject.setUsernames(null);
        try {
            securityProvider.checkAccess(requestInfo);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testEmptySecurity() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(requestInfo.getUser()).thenReturn(userContext);

        Map<String, Object> properties = new HashMap<>();
        properties.put("security", new Security());

        CompiledObject.Operation operation = new CompiledObject.Operation(null, null);
        operation.setProperties(properties);
        when(requestInfo.getOperation()).thenReturn(operation);

        securityProvider.checkAccess(requestInfo);
    }
}
