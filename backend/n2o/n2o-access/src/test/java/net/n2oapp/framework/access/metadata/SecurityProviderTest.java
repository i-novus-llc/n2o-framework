package net.n2oapp.framework.access.metadata;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link SecurityProvider}
 */
class SecurityProviderTest {

    private PermissionApi permissionApi;

    @BeforeEach
    void setUp() {
        permissionApi = mock(PermissionApi.class);
    }

    @Test
    void checkAccessDenied() {
        assertThrows(
                UnauthorizedException.class,
                () -> {
                    SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
                    SecurityObject securityObject = new SecurityObject();
                    securityObject.setDenied(true);
                    Security security = new Security();
                    Map<String, SecurityObject> securityObjectMap = new HashMap<>();
                    securityObjectMap.put("custom", securityObject);
                    security.add(securityObjectMap);
                    securityProvider.checkAccess(security, null);
                    fail("Expected exception to be thrown");
                }
        );
    }

    @Test
    void checkAccessPermitAll() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(true);
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.add(securityObjectMap);
        securityProvider.checkAccess(security, null);
    }

    @Test
    void checkAccessUserIsNotAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        securityProvider.checkAccess(security, userContext);
        securityObject.setAnonymous(false);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    void checkAccessAuthenticated() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(true);
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        securityProvider.checkAccess(security, userContext);
    }

    @Test
    void checkAccessRolesUsernamesPermissionsAreNull() {
        assertThrows(
                AccessDeniedException.class,
                () -> {
                    SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
                    UserContext userContext = new UserContext(new TestContextEngine());
                    when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
                    SecurityObject securityObject = new SecurityObject();
                    securityObject.setDenied(false);
                    securityObject.setPermitAll(false);
                    securityObject.setAnonymous(true);
                    securityObject.setAuthenticated(false);
                    Security security = new Security();
                    Map<String, SecurityObject> securityObjectMap = new HashMap<>();
                    securityObjectMap.put("custom", securityObject);
                    security.add(securityObjectMap);
                    securityProvider.checkAccess(security, userContext);
                    fail("Expected exception to be thrown");
                }
        );
    }

    @Test
    void checkAccessHasRole() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(false);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setRoles(new HashSet<>(Arrays.asList("role2", "role1", "admin")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    void checkAccessHasPermission() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "p2")).thenReturn(false);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0", "p1", "p2")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    void checkAccessHasUsernames() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(true);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n0", "n1", "n2")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n2")).thenReturn(false);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    void checkAccessCombinations() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(false);
        when(permissionApi.hasUsername(userContext, "n1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "r1")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p0")).thenReturn(false);
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(false);
        SecurityObject securityObject = new SecurityObject();
        securityObject.setDenied(false);
        securityObject.setPermitAll(false);
        securityObject.setAnonymous(true);
        securityObject.setAuthenticated(false);
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        Security security = new Security();
        security.add(securityObjectMap);
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n0", "n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0", "p1")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasUsername(userContext, "n0")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasPermission(userContext, "p1")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        when(permissionApi.hasRole(userContext, "r0")).thenReturn(true);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setUsernames(new HashSet<>(Arrays.asList("n1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p0")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(new HashSet<>(Arrays.asList("r0", "r1")));
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
        securityObject.setRoles(null);
        securityObject.setPermissions(null);
        securityObject.setUsernames(null);
        try {
            securityProvider.checkAccess(security, userContext);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }

    @Test
    void checkAccessEmptySecurity() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        securityProvider.checkAccess(new Security(), userContext);
    }

    @Test
    void collectRestrictionsPermitAllFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров применимых ко всем
        ArrayList<N2oObjectFilter> permitAllFilters = new ArrayList<>();
        permitAllFilters.add(new N2oObjectFilter("gender", "man", FilterTypeEnum.EQ, "genderFilter"));
        permitAllFilters.add(new N2oObjectFilter("position", "developer", FilterTypeEnum.EQ, "positionFilter"));
        securityFilters.setPermitAllFilters(permitAllFilters);

        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("gender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("position", "developer", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsAuthenticatedAndAnonymousFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров применимых к авторизованным пользователям
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterTypeEnum.EQ, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterTypeEnum.EQ, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);
        ArrayList<N2oObjectFilter> anonymFilters = new ArrayList<>();
        anonymFilters.add(new N2oObjectFilter("anonymGender", "man", FilterTypeEnum.EQ, "anonymGenderFilter"));
        anonymFilters.add(new N2oObjectFilter("anonymPosition", "developer", FilterTypeEnum.EQ, "anonymPositionFilter"));
        securityFilters.setAnonymousFilters(anonymFilters);
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterTypeEnum.EQ)), is(true));
        //проверка добавления фильтров применимых к неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(2));
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsRoleFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров по ролям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(false);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(true);
        List<N2oObjectFilter> role1Filters = new ArrayList<>();
        role1Filters.add(new N2oObjectFilter("role1Gender", "man", FilterTypeEnum.EQ, "role1GenderFilter"));
        role1Filters.add(new N2oObjectFilter("role1Position", "developer", FilterTypeEnum.EQ, "role1PositionFilter"));
        List<N2oObjectFilter> role2Filters = new ArrayList<>();
        role2Filters.add(new N2oObjectFilter("role2Gender", "man", FilterTypeEnum.EQ, "role2GenderFilter"));
        Map<String, List<N2oObjectFilter>> roleFilters = new HashMap<>();
        roleFilters.put("role1", role1Filters);
        roleFilters.put("role2", role2Filters);
        securityFilters.setRoleFilters(roleFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsPermissionFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров по привелегиям
        when(permissionApi.hasPermission(userContext, "permission1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "permission2")).thenReturn(true);
        List<N2oObjectFilter> permission1Filters = new ArrayList<>();
        permission1Filters.add(new N2oObjectFilter("permission1Gender", "man", FilterTypeEnum.EQ, "permission1GenderFilter"));
        permission1Filters.add(new N2oObjectFilter("permission1Position", "developer", FilterTypeEnum.EQ, "permission1PositionFilter"));
        List<N2oObjectFilter> permission2Filters = new ArrayList<>();
        permission2Filters.add(new N2oObjectFilter("permission2Gender", "man", FilterTypeEnum.EQ, "permission2GenderFilter"));
        Map<String, List<N2oObjectFilter>> permissionFilters = new HashMap<>();
        permissionFilters.put("permission1", permission1Filters);
        permissionFilters.put("permission2", permission2Filters);
        securityFilters.setPermissionFilters(permissionFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(3));
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsUsernameFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка добавления фильтров по пользователям
        when(permissionApi.hasUsername(userContext, "username1")).thenReturn(true);
        Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
        List<N2oObjectFilter> user1Filters = new ArrayList<>();
        user1Filters.add(new N2oObjectFilter("userGender", "man", FilterTypeEnum.EQ, "userGenderFilter"));
        List<N2oObjectFilter> user2Filters = new ArrayList<>();
        user2Filters.add(new N2oObjectFilter("userGender", "woman", FilterTypeEnum.EQ, "userGenderFilter2"));
        userFilters.put("username1", user1Filters);
        userFilters.put("username2", user2Filters);
        securityFilters.setUserFilters(userFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsRemoveUserFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по пользователям

        when(permissionApi.hasUsername(userContext, "username1")).thenReturn(true);
        Map<String, List<N2oObjectFilter>> userFilters = new HashMap<>();
        List<N2oObjectFilter> user1Filters = new ArrayList<>();
        user1Filters.add(new N2oObjectFilter("userGender", "man", FilterTypeEnum.EQ, "userGenderFilter"));
        user1Filters.add(new N2oObjectFilter("userName", "Joe", FilterTypeEnum.EQ, "userGenderFilter2"));
        userFilters.put("username1", user1Filters);
        securityFilters.setUserFilters(userFilters);
        Set<String> user1RemoveFilters = new HashSet<>();
        user1RemoveFilters.add("userGenderFilter");
        Map<String, Set<String>> userRemoveFilters = new HashMap<>();
        userRemoveFilters.put("username1", user1RemoveFilters);
        securityFilters.setRemoveUserFilters(userRemoveFilters);

        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.size(), is(1));
        assertThat(restrictions.contains(new Restriction("userGender", "man", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("userName", "Joe", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsRemovePermissionFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по привелегиям
        when(permissionApi.hasPermission(userContext, "permission1")).thenReturn(true);
        when(permissionApi.hasPermission(userContext, "permission2")).thenReturn(true);
        List<N2oObjectFilter> permission1Filters = new ArrayList<>();
        permission1Filters.add(new N2oObjectFilter("permission1Gender", "man", FilterTypeEnum.EQ, "permission1GenderFilter"));
        permission1Filters.add(new N2oObjectFilter("permission1Position", "developer", FilterTypeEnum.EQ, "permission1PositionFilter"));
        List<N2oObjectFilter> permission2Filters = new ArrayList<>();
        permission2Filters.add(new N2oObjectFilter("permission2Gender", "man", FilterTypeEnum.EQ, "permission2GenderFilter"));
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
        assertThat(restrictions.contains(new Restriction("permission1Gender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("permission1Position", "developer", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("permission2Gender", "man", FilterTypeEnum.EQ)), is(false));
    }

    @Test
    void collectRestrictionsRemoveRoleFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        //проверка удаления фильтров по ролям
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role2")).thenReturn(true);
        List<N2oObjectFilter> role1Filters = new ArrayList<>();
        role1Filters.add(new N2oObjectFilter("role1Gender", "man", FilterTypeEnum.EQ, "role1GenderFilter"));
        role1Filters.add(new N2oObjectFilter("role1Position", "developer", FilterTypeEnum.EQ, "role1PositionFilter"));
        List<N2oObjectFilter> role2Filters = new ArrayList<>();
        role2Filters.add(new N2oObjectFilter("role2Gender", "man", FilterTypeEnum.EQ, "role2GenderFilter"));
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
        assertThat(restrictions.contains(new Restriction("role2Gender", "man", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("role1Gender", "man", FilterTypeEnum.EQ)), is(true));
        assertThat(restrictions.contains(new Restriction("role1Position", "developer", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsRemoveAuthenticatedAndAnonymousFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        ArrayList<N2oObjectFilter> anonymFilters = new ArrayList<>();
        anonymFilters.add(new N2oObjectFilter("anonymGender", "man", FilterTypeEnum.EQ, "anonymGenderFilter"));
        anonymFilters.add(new N2oObjectFilter("anonymPosition", "developer", FilterTypeEnum.EQ, "anonymPositionFilter"));
        securityFilters.setAnonymousFilters(anonymFilters);
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterTypeEnum.EQ, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterTypeEnum.EQ, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);

        //проверка удаления фильтров по неавторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        Set<String> anonymRemoveFilters = new HashSet<>();
        anonymRemoveFilters.add("anonymGenderFilter");
        securityFilters.setRemoveAnonymousFilters(anonymRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("anonymGender", "man", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("anonymPosition", "developer", FilterTypeEnum.EQ)), is(true));

        //проверка удаления фильтров по авторизованным пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Set<String> authRemoveFilters = new HashSet<>();
        authRemoveFilters.add("authGenderFilter");
        securityFilters.setRemoveAuthenticatedFilters(authRemoveFilters);
        restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void collectRestrictionsRemovePermitAllFilters() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        ArrayList<N2oObjectFilter> authFilters = new ArrayList<>();
        authFilters.add(new N2oObjectFilter("authGender", "man", FilterTypeEnum.EQ, "authGenderFilter"));
        authFilters.add(new N2oObjectFilter("authPosition", "developer", FilterTypeEnum.EQ, "authPositionFilter"));
        securityFilters.setAuthenticatedFilters(authFilters);

        //проверка удаления фильтров по всем пользователям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        Set<String> permitAllRemoveFilters = new HashSet<>();
        permitAllRemoveFilters.add("authPositionFilter");
        securityFilters.setRemovePermitAllFilters(permitAllRemoveFilters);
        List<Restriction> restrictions = securityProvider.collectRestrictions(securityFilters, userContext);
        assertThat(restrictions.contains(new Restriction("authPosition", "developer", FilterTypeEnum.EQ)), is(false));
        assertThat(restrictions.contains(new Restriction("authGender", "man", FilterTypeEnum.EQ)), is(true));
    }

    @Test
    void checkQueryRestrictions() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        SecurityProvider notStrictSecurityProvider = new SecurityProvider(permissionApi, false);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();
        Map<String, Map<FilterTypeEnum, N2oQuery.Filter>> filtersMap = Map.of(
                "foo", Map.of(FilterTypeEnum.EQ, new N2oQuery.Filter("foo.val", FilterTypeEnum.EQ),
                        FilterTypeEnum.NOT_EQ, new N2oQuery.Filter("foo.val", FilterTypeEnum.NOT_EQ)),
                "name", Map.of(FilterTypeEnum.IS_NOT_NULL, new N2oQuery.Filter("name", FilterTypeEnum.IS_NOT_NULL),
                        FilterTypeEnum.EQ, new N2oQuery.Filter("name", FilterTypeEnum.EQ)),
                "surname", Map.of(FilterTypeEnum.EQ_OR_IS_NULL, new N2oQuery.Filter("surname", FilterTypeEnum.EQ_OR_IS_NULL)),
                "age", Map.of(FilterTypeEnum.IS_NULL, new N2oQuery.Filter("age", FilterTypeEnum.IS_NULL)),
                "bar", Map.of(FilterTypeEnum.IN, new N2oQuery.Filter("bar", FilterTypeEnum.IN)),
                "list", Map.of(FilterTypeEnum.CONTAINS, new N2oQuery.Filter("list", FilterTypeEnum.CONTAINS))
        );

        securityFilters.setAuthenticatedFilters(Arrays.asList(
                new N2oObjectFilter("foo", "1", FilterTypeEnum.EQ, "filter1"),
                new N2oObjectFilter("name", FilterTypeEnum.IS_NOT_NULL, "filter7"),
                new N2oObjectFilter("surname", "1", FilterTypeEnum.EQ_OR_IS_NULL, "filter6")));
        securityFilters.setAnonymousFilters(Arrays.asList(
                new N2oObjectFilter("age", FilterTypeEnum.IS_NULL, "filter8"),
                new N2oObjectFilter("foo", "1", FilterTypeEnum.NOT_EQ, "filter2")));
        securityFilters.setRoleFilters(Collections.singletonMap("role1", Collections.singletonList(
                new N2oObjectFilter("bar", new String[]{"1", "2", "3"}, FilterTypeEnum.IN, "filter3"))));
        securityFilters.setPermissionFilters(Collections.singletonMap("permission1", Collections.singletonList(
                new N2oObjectFilter("list", new String[]{"1", "2", "#{three}"}, FilterTypeEnum.CONTAINS, "filter4"))));
        securityFilters.setUserFilters(Collections.singletonMap("username1", Collections.singletonList(
                new N2oObjectFilter("name", "#{username}", FilterTypeEnum.EQ, "filter5"))));

        //аутентифицирован
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        //foo == 1 and name != null and surname == null
        securityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 1).add("name", "Ivan"),
                securityFilters, userContext, filtersMap);
        //foo != 1
        notStrictSecurityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 1).add("name", "Ivan"),
                securityFilters, userContext, filtersMap);
        try {
            securityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 2).add("name", "Ivan"),
                    securityFilters, userContext, filtersMap);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //foo == null
        notStrictSecurityProvider.checkQueryRestrictions(new DataSet(),
                securityFilters, userContext, filtersMap);
        try {
            securityProvider.checkQueryRestrictions(new DataSet().add("name", "Ivan"),
                    securityFilters, userContext, filtersMap);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }

        //анонимный доступ
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        //foo != 1
        securityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 2),
                securityFilters, userContext, filtersMap);
        //foo == 1
        try {
            securityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 1),
                    securityFilters, userContext, filtersMap);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //age != null
        try {
            securityProvider.checkQueryRestrictions(new DataSet().add("foo.val", 3).add("age", 10),
                    securityFilters, userContext, filtersMap);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("age"));
        }

        //доступ аутентифицированным и по ролям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        //bar in (1, 2, 3)
        try {
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan"),
                    securityFilters, userContext, filtersMap);
        } catch (AccessDeniedException e) {
            fail();
        }
        //bar not in (1, 2, 3)
        try {
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 4)
                            .add("name", "Ivan"),
                    securityFilters, userContext, filtersMap);
            fail();
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
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(3, 2, 1, 4)),
                    securityFilters, userContext, filtersMap);
        } catch (AccessDeniedException e) {
            fail();
        }

        //list not contains (1, 2, 3)
        try {
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(1, 2)),
                    securityFilters, userContext, filtersMap);
            fail();
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
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Joe"),
                    securityFilters, userContext, filtersMap);
        } catch (AccessDeniedException e) {
            fail();
        }
        //name != #{username}
        try {
            securityProvider.checkQueryRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Doe"),
                    securityFilters, userContext, filtersMap);
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("name"));
        }
    }

    @Test
    void checkObjectRestrictions() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi, true);
        SecurityProvider notStrictSecurityProvider = new SecurityProvider(permissionApi, false);
        UserContext userContext = new UserContext(new TestContextEngine());
        SecurityFilters securityFilters = new SecurityFilters();

        securityFilters.setAuthenticatedFilters(Arrays.asList(
                new N2oObjectFilter("foo", "1", FilterTypeEnum.EQ, "foo.val"),
                new N2oObjectFilter("name", FilterTypeEnum.IS_NOT_NULL, "name"),
                new N2oObjectFilter("surname", "1", FilterTypeEnum.EQ_OR_IS_NULL, "surname")));
        securityFilters.setAnonymousFilters(Arrays.asList(
                new N2oObjectFilter("age", FilterTypeEnum.IS_NULL, "age"),
                new N2oObjectFilter("foo", "1", FilterTypeEnum.NOT_EQ, "foo.val")));
        securityFilters.setRoleFilters(Collections.singletonMap("role1", Collections.singletonList(
                new N2oObjectFilter("bar", new String[]{"1", "2", "3"}, FilterTypeEnum.IN, "bar"))));
        securityFilters.setPermissionFilters(Collections.singletonMap("permission1", Collections.singletonList(
                new N2oObjectFilter("list", new String[]{"1", "2", "#{three}"}, FilterTypeEnum.CONTAINS, "list"))));
        securityFilters.setUserFilters(Collections.singletonMap("username1", Collections.singletonList(
                new N2oObjectFilter("name", "#{username}", FilterTypeEnum.EQ, "name"))));

        //аутентифицирован
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        //foo == 1 and name != null and surname == null
        securityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 1).add("name", "Ivan"),
                securityFilters, userContext);
        //foo != 1
        notStrictSecurityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 1).add("name", "Ivan"),
                securityFilters, userContext);
        try {
            securityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 2).add("name", "Ivan"),
                    securityFilters, userContext);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //foo == null
        notStrictSecurityProvider.checkObjectRestrictions(new DataSet(),
                securityFilters, userContext);
        try {
            securityProvider.checkObjectRestrictions(new DataSet().add("name", "Ivan"),
                    securityFilters, userContext);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }

        //анонимный доступ
        when(permissionApi.hasAuthentication(userContext)).thenReturn(false);
        //foo != 1
        securityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 2),
                securityFilters, userContext);
        //foo == 1
        try {
            securityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 1),
                    securityFilters, userContext);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("foo"));
        }
        //age != null
        try {
            securityProvider.checkObjectRestrictions(new DataSet().add("foo.val", 3).add("age", 10),
                    securityFilters, userContext);
            fail();
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("age"));
        }

        //доступ аутентифицированным и по ролям
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "role1")).thenReturn(true);
        //bar in (1, 2, 3)
        try {
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            fail();
        }
        //bar not in (1, 2, 3)
        try {
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 4)
                            .add("name", "Ivan"),
                    securityFilters, userContext);
            fail();
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
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(3, 2, 1, 4)),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            fail();
        }

        //list not contains (1, 2, 3)
        try {
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("name", "Ivan")
                            .add("list", Arrays.asList(1, 2)),
                    securityFilters, userContext);
            fail();
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
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Joe"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            fail();
        }
        //name != #{username}
        try {
            securityProvider.checkObjectRestrictions(new DataSet()
                            .add("foo.val", 1)
                            .add("bar", 2)
                            .add("list", Arrays.asList(3, 2, 1, 4))
                            .add("name", "Doe"),
                    securityFilters, userContext);
        } catch (AccessDeniedException e) {
            assertThat(e.getMessage(), endsWith("name"));
        }
    }
}
