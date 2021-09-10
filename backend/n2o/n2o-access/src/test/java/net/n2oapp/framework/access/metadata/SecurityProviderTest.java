package net.n2oapp.framework.access.metadata;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link SecurityProvider}
 */
public class SecurityProviderTest {

    private PermissionApi permissionApi;

    @Before
    public void setUp() {
        permissionApi = mock(PermissionApi.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void checkAccessDenied() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        ;
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityProvider.checkAccess(security, null);
        Assert.fail("Expected exception to be thrown");
    }

    @Test
    public void checkAccessPermitAll() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityProvider.checkAccess(security, null);
    }

    @Test
    public void checkAccessUserIsNotAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityProvider.checkAccess(security, userContext);
        securityObject.setAnonymous(false);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    public void checkAccessAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(true);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityProvider.checkAccess(security, userContext);
    }

    @Test(expected = AccessDeniedException.class)
    public void checkAccessRolesUsernamesPermissionsAreNull() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityProvider.checkAccess(security, userContext);
        Assert.fail("Expected exception to be thrown");
    }

    @Test
    public void checkAccessHasRole() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(false);
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
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void checkAccessHasPermission() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p2")).thenReturn(false);
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
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void checkAccessHasUsernames() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(true);
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
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void checkAccessCombinations() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r1")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);
        Security.SecurityObject securityObject = new Security.SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, Security.SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.setSecurityMap(securityObjectMap);
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n0", "n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0", "p1")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(null);
        securityObject.setPermissions(null);
        securityObject.setUsernames(null);
        try {
            securityProvider.checkAccess(security, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    public void checkAccessEmptySecurity() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        securityProvider.checkAccess(new Security(), userContext);
    }

    @Test
    public void collectRestrictionsPermitAllFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров применимых ко всем
        ArrayList<N2oObjectFilter> permitAllFilters = new ArrayList<>();
        permitAllFilters.add(new N2oObjectFilter("gender", "man", FilterType.eq, "genderFilter"));
        permitAllFilters.add(new N2oObjectFilter("position", "developer", FilterType.eq, "positionFilter"));
        securityFilters.setPermitAllFilters(permitAllFilters);

        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("position", "developer", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsAuthenticatedAndAnonymousFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
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
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(true));
        //проверка добавления фильтров применимых к неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsRoleFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
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
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsPermissionFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
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
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(3));
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsUsernameFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров по пользователям
        when(permissionApi.hasUsername(userContext, "username1")).thenReturn(true);
        Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
        List<N2oObjectFilter> user1Filters = new ArrayList<>();
        user1Filters.add(new N2oObjectFilter("userGender", "man", FilterType.eq, "userGenderFilter"));
        List<N2oObjectFilter> user2Filters = new ArrayList<>();
        user2Filters.add(new N2oObjectFilter("userGender", "woman", FilterType.eq, "userGenderFilter2"));
        userFilters.put("username1", user1Filters);
        userFilters.put("username2", user2Filters);
        securityFilters.setUserFilters(userFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsRemoveUserFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по пользователям

        when(permissionApi.hasUsername(userContext, "username1")).thenReturn(true);
        Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
        List<N2oObjectFilter> user1Filters = new ArrayList<>();
        user1Filters.add(new N2oObjectFilter("userGender", "man", FilterType.eq, "userGenderFilter"));
        user1Filters.add(new N2oObjectFilter("userName", "Joe", FilterType.eq, "userGenderFilter2"));
        userFilters.put("username1", user1Filters);
        securityFilters.setUserFilters(userFilters);
        Set<String> user1RemoveFilters = new HashSet<>();
        user1RemoveFilters.add("userGenderFilter");
        Map<String, Set<String>> userRemoveFilters = new HashMap<>();
        userRemoveFilters.put("username1", user1RemoveFilters);
        securityFilters.setRemoveUserFilters(userRemoveFilters);

        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("userName", "Joe", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsRemovePermissionFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по привелегиям
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

        Set<String> permission1RemoveFilters = new HashSet<>();
        permission1RemoveFilters.add("permission2GenderFilter");
        permission1RemoveFilters.add("permission1PositionFilter");
        Map<String, Set<String>> permissionRemoveFilters = new HashMap<>();
        permissionRemoveFilters.put("permission1", permission1RemoveFilters);
        securityFilters.setRemovePermissionFilters(permissionRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterType.eq)), is(false));
    }

    @Test
    public void collectRestrictionsRemoveRoleFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по ролям
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
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

        Set<String> role2RemoveFilters = new HashSet<>();
        role2RemoveFilters.add("role2GenderFilter");
        Map<String, Set<String>> roleRemoveFilters = new HashMap<>();
        roleRemoveFilters.put("role2", role2RemoveFilters);
        securityFilters.setRemoveRoleFilters(roleRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("role1Gender", "man", FilterType.eq)), is(true));
        assertThat(restrictions.contains(new Restriction("role1Position", "developer", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsRemoveAuthenticatedAndAnonymousFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        ArrayList<N2oObjectFilter> anonymFilters = new ArrayList<>();
        anonymFilters.add(new N2oObjectFilter("anonymGender", "man", FilterType.eq, "anonymGenderFilter"));
        anonymFilters.add(new N2oObjectFilter("anonymPosition", "developer", FilterType.eq, "anonymPositionFilter"));
        securityFilters.setAnonymousFilters(anonymFilters);
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterType.eq, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterType.eq, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);

        //проверка удаления фильтров по неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        Set<String> anonymRemoveFilters = new HashSet<>();
        anonymRemoveFilters.add("anonymGenderFilter");
        securityFilters.setRemoveAnonymousFilters(anonymRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterType.eq)), is(true));

        //проверка удаления фильтров по авторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Set<String> authRemoveFilters = new HashSet<>();
        authRemoveFilters.add("authGenderFilter");
        securityFilters.setRemoveAuthenticatedFilters(authRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(true));
    }

    @Test
    public void collectRestrictionsRemovePermitAllFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterType.eq, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterType.eq, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);

        //проверка удаления фильтров по всем пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Set<String> permitAllRemoveFilters = new HashSet<>();
        permitAllRemoveFilters.add("authPositionFilter");
        securityFilters.setRemovePermitAllFilters(permitAllRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterType.eq)), is(false));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterType.eq)), is(true));
    }

    @Test
    public void checkRestrictions() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        SecurityProvider notStrictSecurityProvider = new SecurityProvider(permissionApi, false);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();

        securityFilters.setAuthenticatedFilters(Arrays.asList(
                new N2oObjectFilter("foo", "1", FilterType.eq, "filter1"),
                new N2oObjectFilter("name", FilterType.isNotNull, "filter7"),
                new N2oObjectFilter("surname", "1", FilterType.eqOrIsNull, "filter6")));
        securityFilters.setAnonymousFilters(Arrays.asList(
                new N2oObjectFilter("age", FilterType.isNull, "filter8"),
                new N2oObjectFilter("foo", "1", FilterType.notEq, "filter2")));
        securityFilters.setRoleFilters(Collections.singletonMap("role1", Collections.singletonList(
                new N2oObjectFilter("bar", new String[]{"1", "2", "3"}, FilterType.in, "filter3"))));
        securityFilters.setPermissionFilters(Collections.singletonMap("permission1", Collections.singletonList(
                new N2oObjectFilter("list", new String[]{"1", "2", "#{three}"}, FilterType.contains, "filter4"))));
        securityFilters.setUserFilters(Collections.singletonMap("username1", Collections.singletonList(
                new N2oObjectFilter("name", "#{username}", FilterType.eq, "filter5"))));

        //аутентифицирован
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        //foo == 1 and name != null and surname == null
        securityProvider.checkRestrictions(new DataSet().add("foo", 1).add("name", "Ivan"), securityFilters, userContext);
        //foo != 1
        notStrictSecurityProvider.checkRestrictions(new DataSet().add("foo", 1).add("name", "Ivan"), securityFilters, userContext);
        try {
            securityProvider.checkRestrictions(new DataSet().add("name", "Ivan").add("foo", 2), securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //foo == null
        notStrictSecurityProvider.checkRestrictions(new DataSet(), securityFilters, userContext);
        try {
            securityProvider.checkRestrictions(new DataSet().add("name", "Ivan"), securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }

        //анонимный доступ
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        //foo != 1
        securityProvider.checkRestrictions(new DataSet().add("foo", 2), securityFilters, userContext);
        //foo == 1
        try {
            securityProvider.checkRestrictions(new DataSet().add("foo", 1), securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //age != null
        try {
            securityProvider.checkRestrictions(new DataSet().add("foo", 3).add("age", 10), securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("age"));
        }

        //доступ аутентифицированным и по ролям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        //bar in (1, 2, 3)
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 2)
                            .add("name", "Ivan"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            Assert.fail();
        }
        //bar not in (1, 2, 3)
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 4)
                            .add("name", "Ivan"),
                    securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("bar"));
        }

        //доступ аутентифицированным, по ролям и по полномочиям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "permission1")).thenReturn(true);
        userContext.set("three", 3);
        //list contains (1, 2, 3)
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(3, 2, 1, 4)),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            Assert.fail();
        }

        //list not contains (1, 2, 3)
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(1, 2)),
                    securityFilters, userContext);
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("list"));
        }

        //доступ аутентифицированным, по ролям, по полномочиям, по имени пользователя
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "permission1")).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "username1")).thenReturn(true);
        userContext.set("username", "Joe");
        //name == #{username}
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Joe"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            Assert.fail();
        }
        //name != #{username}
        try {
            securityProvider.checkRestrictions(new DataSet()
                            .add("foo", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Doe"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("name"));
        }
    }
}
