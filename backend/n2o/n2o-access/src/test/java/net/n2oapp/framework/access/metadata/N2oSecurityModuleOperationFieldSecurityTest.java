package net.n2oapp.framework.access.metadata;

import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.integration.N2oSecurityModule;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.ui.ActionRequestInfo;
import net.n2oapp.framework.api.ui.ActionResponseInfo;
import net.n2oapp.framework.api.user.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.access.metadata.Security.IN_FIELD_SECURITY_PROP_NAME;
import static net.n2oapp.framework.access.metadata.Security.OUT_FIELD_SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class N2oSecurityModuleOperationFieldSecurityTest {

    private PermissionApi permissionApi;
    private N2oSecurityModule module;

    @BeforeEach
    void setUp() {
        permissionApi = mock(PermissionApi.class);
        module = new N2oSecurityModule(new SecurityProvider(permissionApi, true), false);
    }

    @Test
    void inaccessibleInFieldRemovedFromInputInProcessAction() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(false);

        DataSet input = new DataSet();
        input.put("name", "Иван");
        input.put("salary", 100000);

        module.processAction(
                requestInfo(operation(IN_FIELD_SECURITY_PROP_NAME, "salary", securityFor(Set.of("hr"), null))),
                new ActionResponseInfo(),
                input);

        assertThat("salary must be removed from input", input.containsKey("salary"), is(false));
        assertThat(input.get("name"), is("Иван"));
    }

    @Test
    void accessibleInFieldKeptInProcessAction() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(true);

        DataSet input = new DataSet();
        input.put("salary", 100000);

        module.processAction(
                requestInfo(operation(IN_FIELD_SECURITY_PROP_NAME, "salary", securityFor(Set.of("hr"), null))),
                new ActionResponseInfo(),
                input);

        assertThat(input.get("salary"), is(100000));
    }

    @Test
    void inaccessibleFieldsRemovedFromResultInProcessActionResult() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(false);
        when(permissionApi.hasPermission(any(), eq("view-sensitive"))).thenReturn(false);

        DataSet inResult = new DataSet();
        inResult.put("salary", 100000);
        inResult.put("id", 1);

        module.processActionResult(
                requestInfo(operation(IN_FIELD_SECURITY_PROP_NAME, "salary", securityFor(Set.of("hr"), null))),
                new ActionResponseInfo(),
                inResult);

        assertThat("salary must not be returned", inResult.containsKey("salary"), is(false));
        assertThat(inResult.get("id"), is(1));

        DataSet outResult = new DataSet();
        outResult.put("secret", "xxx");
        outResult.put("id", 1);

        module.processActionResult(
                requestInfo(operation(OUT_FIELD_SECURITY_PROP_NAME, "secret", securityFor(null, Set.of("view-sensitive")))),
                new ActionResponseInfo(),
                outResult);

        assertThat("secret must not be returned", outResult.containsKey("secret"), is(false));
        assertThat(outResult.get("id"), is(1));
    }

    @Test
    void nestedInFieldsInReferenceAndListAreFilteredInProcessAction() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("hr"))).thenReturn(false);
        when(permissionApi.hasRole(any(), eq("admin"))).thenReturn(false);

        DataSet ref = new DataSet();
        ref.put("salary", 100000);
        ref.put("code", "A1");

        DataSet item1 = new DataSet();
        item1.put("amount", 500);
        DataSet item2 = new DataSet();
        item2.put("amount", 700);
        DataList items = new DataList();
        items.add(item1);
        items.add(item2);

        DataSet input = new DataSet();
        input.put("ref", ref);
        input.put("items", items);
        input.put("name", "Иван");

        Map<String, Security> inSecurity = new HashMap<>();
        inSecurity.put("ref.salary", securityFor(Set.of("hr"), null));
        inSecurity.put("items.amount", securityFor(Set.of("admin"), null));

        module.processAction(
                requestInfo(operation(IN_FIELD_SECURITY_PROP_NAME, inSecurity)),
                new ActionResponseInfo(),
                input
        );

        assertThat("ref.salary must be removed", ref.containsKey("salary"), is(false));
        assertThat("ref.code must remain", ref.get("code"), is("A1"));
        assertThat("items[0].amount must be removed", item1.containsKey("amount"), is(false));
        assertThat("items[1].amount must be removed", item2.containsKey("amount"), is(false));
        assertThat("name must remain", input.get("name"), is("Иван"));
    }

    @Test
    void nullResultHandledGracefully() {
        try {
            module.processActionResult(
                    requestInfo(operation(OUT_FIELD_SECURITY_PROP_NAME, "secret", securityFor(null, Set.of("p")))),
                    new ActionResponseInfo(),
                    null);
        } catch (Exception e) {
            fail();
        }
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

    private CompiledObject.Operation operation(String propName, String fieldId, Security security) {
        Map<String, Security> fieldSec = new HashMap<>();
        fieldSec.put(fieldId, security);
        return operation(propName, fieldSec);
    }

    private CompiledObject.Operation operation(String propName, Map<String, Security> fieldSec) {
        CompiledObject.Operation operation = new CompiledObject.Operation();
        Map<String, Object> props = new HashMap<>();
        props.put(propName, fieldSec);
        operation.setProperties(props);
        return operation;
    }

    @SuppressWarnings("unchecked")
    private ActionRequestInfo<DataSet> requestInfo(CompiledObject.Operation operation) {
        ActionRequestInfo<DataSet> info = new ActionRequestInfo<>();
        info.setOperation(operation);
        info.setUser(new UserContext(new TestContextEngine()));
        return info;
    }
}
