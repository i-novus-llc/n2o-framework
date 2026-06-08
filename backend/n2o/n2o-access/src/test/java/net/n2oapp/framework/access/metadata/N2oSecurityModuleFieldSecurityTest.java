package net.n2oapp.framework.access.metadata;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.integration.N2oSecurityModule;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.api.ui.QueryResponseInfo;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static net.n2oapp.framework.access.metadata.Security.FIELD_SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class N2oSecurityModuleFieldSecurityTest {

    private PermissionApi permissionApi;
    private N2oSecurityModule module;

    @BeforeEach
    void setUp() {
        permissionApi = mock(PermissionApi.class);
        module = new N2oSecurityModule(new SecurityProvider(permissionApi, true), false);
    }

    @Test
    void fieldRemovedFromDataSetWhenUserLacksRole() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(false);

        DataSet row = new DataSet();
        row.put("name", "Иван");
        row.put("salary", 100000);

        module.processQueryResult(
                requestInfo(queryWithFieldSecurity("salary", securityFor(Set.of("hr"), null))),
                new QueryResponseInfo(),
                page(row)
        );

        assertThat("salary must be removed", row.containsKey("salary"), is(false));
        assertThat("name must remain unchanged", row.get("name"), is("Иван"));
    }

    @Test
    void fieldKeptWhenUserHasRequiredRole() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(true);

        DataSet row = new DataSet();
        row.put("name", "Иван");
        row.put("salary", 100000);

        module.processQueryResult(
                requestInfo(queryWithFieldSecurity("salary", securityFor(Set.of("hr"), null))),
                new QueryResponseInfo(),
                page(row)
        );

        assertThat("salary must be kept", row.get("salary"), is(100000));
        assertThat("name must remain unchanged", row.get("name"), is("Иван"));
    }

    @Test
    void fieldFilteringWorksForAllRowsInListResult() {
        // authenticated user, but no matching roles — both rows lose salary
        when(permissionApi.hasAuthentication(any())).thenReturn(true);

        DataSet row1 = new DataSet();
        row1.put("name", "Иван");
        row1.put("salary", 100000);

        DataSet row2 = new DataSet();
        row2.put("name", "Мария");
        row2.put("salary", 200000);

        module.processQueryResult(
                requestInfo(queryWithFieldSecurity("salary", securityFor(Set.of("hr"), null))),
                new QueryResponseInfo(),
                page(row1, row2)
        );

        assertThat(row1.containsKey("salary"), is(false));
        assertThat(row2.containsKey("salary"), is(false));
        assertThat(row1.get("name"), is("Иван"));
        assertThat(row2.get("name"), is("Мария"));
    }

    @Test
    void nestedFieldsInReferenceAndListAreFilteredByFieldSecurity() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(false);
        when(permissionApi.hasRole(any(), eq("admin"))).thenReturn(false);

        DataSet ref = new DataSet();
        ref.put("name", "Иван");
        ref.put("code", "A1");

        DataSet item1 = new DataSet();
        item1.put("salary", 100000);
        DataSet item2 = new DataSet();
        item2.put("salary", 200000);
        DataList items = new DataList();
        items.add(item1);
        items.add(item2);

        DataSet row = new DataSet();
        row.put("ref", ref);
        row.put("items", items);
        row.put("title", "Тест");

        Map<String, Security> fieldSecurity = new HashMap<>();
        fieldSecurity.put("ref.name", securityFor(Set.of("hr"), null));
        fieldSecurity.put("items.salary", securityFor(Set.of("admin"), null));

        module.processQueryResult(
                requestInfo(queryWithFieldSecurity(fieldSecurity)),
                new QueryResponseInfo(),
                page(row)
        );

        assertThat("ref.name must be removed", ref.containsKey("name"), is(false));
        assertThat("ref.code must remain", ref.get("code"), is("A1"));
        assertThat("items[0].salary must be removed", item1.containsKey("salary"), is(false));
        assertThat("items[1].salary must be removed", item2.containsKey("salary"), is(false));
        assertThat("title must remain", row.get("title"), is("Тест"));
    }

    @Test
    void fieldsWithoutSecurityAreUnaffected() {
        DataSet row = new DataSet();
        row.put("name", "Иван");
        row.put("salary", 100000);

        // query has no fieldSecurity at all
        module.processQueryResult(
                requestInfo(new CompiledQuery()),
                new QueryResponseInfo(),
                page(row)
        );

        assertThat(row.get("name"), is("Иван"));
        assertThat(row.get("salary"), is(100000));
    }

    // --- helpers ---

    private Security securityFor(Set<String> roles, Set<String> permissions) {
        SecurityObject secObj = new SecurityObject();
        secObj.setRoles(roles);
        secObj.setPermissions(permissions);
        Security security = new Security();
        Map<String, SecurityObject> secMap = new HashMap<>();
        secMap.put("custom", secObj);
        security.add(secMap);
        return security;
    }

    private CompiledQuery queryWithFieldSecurity(String fieldId, Security security) {
        Map<String, Security> fieldSec = new HashMap<>();
        fieldSec.put(fieldId, security);
        return queryWithFieldSecurity(fieldSec);
    }

    private CompiledQuery queryWithFieldSecurity(Map<String, Security> fieldSec) {
        CompiledQuery query = new CompiledQuery();
        Map<String, Object> props = new HashMap<>();
        props.put(FIELD_SECURITY_PROP_NAME, fieldSec);
        query.setProperties(props);
        return query;
    }

    private QueryRequestInfo requestInfo(CompiledQuery query) {
        QueryRequestInfo info = new QueryRequestInfo();
        info.setQuery(query);
        info.setMode(DefaultValuesModeEnum.QUERY);
        info.setSize(100);
        info.setUser(new UserContext(new TestContextEngine()));
        return info;
    }

    @SafeVarargs
    private <T> CollectionPage<T> page(T... rows) {
        CollectionPage<T> page = new CollectionPage<>();
        page.setCollection(Arrays.asList(rows));
        return page;
    }
}
