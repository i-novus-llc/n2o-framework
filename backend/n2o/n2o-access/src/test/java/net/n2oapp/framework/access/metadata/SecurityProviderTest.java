package net.n2oapp.framework.access.metadata;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityProviderTest {

    private PermissionApi permissionApi;

    @Before
    public void setUp() {
        permissionApi = mock(PermissionApi.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void testDenied() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);;
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        securityProvider.checkAccess(securityObjectMap, null);
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
        securityProvider.checkAccess(securityObjectMap, null);
    }

    @Test
    public void testUserIsNotAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityProvider.checkAccess(securityObjectMap, userContext);
        securityObject.setAnonymous(false);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    public void testAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityProvider.checkAccess(securityObjectMap, userContext);
    }

    @Test(expected = AccessDeniedException.class)
    public void testRolesUsernamesPermissionsAreNull() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityProvider.checkAccess(securityObjectMap, userContext);
        Assert.fail("Expected exception to be thrown");
    }

    @Test
    public void testHasRole() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityObject.setRoles(new HashSet<>(Arrays.asList("role2", "role1", "admin")));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(false);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testHasPermission() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());

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
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0", "p1", "p2")));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testHasUsernames() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n0", "n1", "n2")));
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        properties.put("security", security);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(false);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testCombinations() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
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
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n0", "n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0", "p1")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(true);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(true);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r1")));
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(null);
        securityObject.setPermissions(null);
        securityObject.setUsernames(null);
        try {
            securityProvider.checkAccess(securityObjectMap, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void testEmptySecurity() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        securityProvider.checkAccess(new Security().getSecurityMap(), userContext);
    }

    @Test
    public void testCollectRestrictions() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        //проверка добавления фильтров применимых ко всем
        SecurityFilters securityFilters = new SecurityFilters();
        ArrayList<N2oObjectFilter> permitAllFilters = new ArrayList<>();
        permitAllFilters.add(new N2oObjectFilter("gender", "man", FilterType.eq, "genderFilter"));
        permitAllFilters.add(new N2oObjectFilter("position", "developer", FilterType.eq, "positionFilter"));
        securityFilters.setPermitAllFilters(permitAllFilters);
        Set<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("position", "developer", FilterType.eq)), is(true));
        //проверка добавления фильтров применимых к авторизованным пользователям
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterType.eq, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterType.eq, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);
        ArrayList<N2oObjectFilter> anonymFilters = new ArrayList<>();
        anonymFilters.add(new N2oObjectFilter("anonymGender", "man", FilterType.eq, "anonymGenderFilter"));
        anonymFilters.add(new N2oObjectFilter("anonymPosition", "developer", FilterType.eq, "anonymPositionFilter"));
        securityFilters.setAnonymousFilters(anonymFilters);
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(4));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(true));
        //проверка добавления фильтров применимых к неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(4));
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterType.eq)), is(true));
        //проверка добавления фильтров по ролям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(true);
        List<N2oObjectFilter> role1Filters = new ArrayList<>();
        role1Filters.add(new N2oObjectFilter("role1Gender", "man", FilterType.eq, "role1GenderFilter"));
        role1Filters.add(new N2oObjectFilter("role1Position", "developer", FilterType.eq, "role1PositionFilter"));
        List<N2oObjectFilter> role2Filters = new ArrayList<>();
        role2Filters.add(new N2oObjectFilter("role2Gender", "man", FilterType.eq, "role2GenderFilter"));
        Map<String, List<N2oObjectFilter>> roleFilters = new HashMap<>();
        roleFilters.put("role1", role1Filters);
        roleFilters.put("role2", role2Filters);
        securityFilters.setRoleFilters(roleFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(5));
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterType.eq)), is(true));
        //проверка добавления фильтров по привелегиям
        when(permissionApi.hasPermission(userContext, "permission1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "permission2")).thenReturn(true);
        List<N2oObjectFilter> permission1Filters = new ArrayList<>();
        permission1Filters.add(new N2oObjectFilter("permission1Gender", "man", FilterType.eq, "permission1GenderFilter"));
        permission1Filters.add(new N2oObjectFilter("permission1Position", "developer", FilterType.eq, "permission1PositionFilter"));
        List<N2oObjectFilter> permission2Filters = new ArrayList<>();
        permission2Filters.add(new N2oObjectFilter("permission2Gender", "man", FilterType.eq, "permission2GenderFilter"));
        Map<String, List<N2oObjectFilter>> permissionFilters = new HashMap<>();
        permissionFilters.put("permission1", permission1Filters);
        permissionFilters.put("permission2", permission2Filters);
        securityFilters.setPermissionFilters(permissionFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(8));
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterType.eq)), is(true));
        //проверка добавления фильтров по пользователям
        when(permissionApi.hasUsername(userContext, "username")).thenReturn(true);
        List<N2oObjectFilter> user1Filters = new ArrayList<>();
        user1Filters.add(new N2oObjectFilter("userGender", "man", FilterType.eq, "userGenderFilter"));
        Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
        userFilters.put("username", user1Filters);
        securityFilters.setUserFilters(userFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(9));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterType.eq)), is(true));

        //проверка удаления фильтров по пользователям
        Set<String> user1RemoveFilters = new HashSet<>();
        user1RemoveFilters.add("userGenderFilter");
        Map<String, Set<String>> userRemoveFilters = new HashMap<>();
        userRemoveFilters.put("username", user1RemoveFilters);
        securityFilters.setRemoveUserFilters(userRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(8));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterType.eq)), is(false));
        //проверка удаления фильтров по привелегиям
        Set<String> permission1RemoveFilters = new HashSet<>();
        permission1RemoveFilters.add("permission2GenderFilter");
        permission1RemoveFilters.add("permission1PositionFilter");
        Map<String, Set<String>> permissionRemoveFilters = new HashMap<>();
        permissionRemoveFilters.put("permission1", permission1RemoveFilters);
        securityFilters.setRemovePermissionFilters(permissionRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(6));
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterType.eq)), is(false));
        //проверка удаления фильтров по ролям
        Set<String> role2RemoveFilters = new HashSet<>();
        role2RemoveFilters.add("role2GenderFilter");
        Map<String, Set<String>> roleRemoveFilters = new HashMap<>();
        roleRemoveFilters.put("role2", role2RemoveFilters);
        securityFilters.setRemoveRoleFilters(roleRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(5));
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterType.eq)), is(false));
        //проверка удаления фильтров по неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        Set<String> anonymRemoveFilters = new HashSet<>();
        anonymRemoveFilters.add("anonymGenderFilter");
        securityFilters.setRemoveAnonymousFilters(anonymRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(4));
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterType.eq)), is(true));
        //проверка удаления фильтров по авторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Set<String> authRemoveFilters = new HashSet<>();
        authRemoveFilters.add("authGenderFilter");
        securityFilters.setRemoveAuthenticatedFilters(authRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(4));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(true));
        //проверка удаления фильтров по всем пользователям
        Set<String> permitAllRemoveFilters = new HashSet<>();
        permitAllRemoveFilters.add("authPositionFilter");
        permitAllRemoveFilters.add("positionFilter");
        securityFilters.setRemovePermitAllFilters(permitAllRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("position", "developer", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(false));
    }
}
