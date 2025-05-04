package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.SecurityPageBinder;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.access.mock.PermissionApiMock;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityPageBinderTest extends SourceCompileTestBase {

    private PermissionApiMock permissionApi;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);

        permissionApi = mock(PermissionApiMock.class);

        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                        new N2oWidgetsPack(), new N2oRegionsPack(), new AccessSchemaPack())
                .binders(new SecurityPageBinder(new SecurityProvider(permissionApi, false)))
                .properties("n2o.access.schema.id=testSecurityPageBinder");
    }

    @Test
    void hasAuthenticationTest() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);
        when(permissionApi.hasRole(any(), eq("admin"))).thenReturn(true);

        assertDoesNotThrow(() -> compile("net/n2oapp/framework/access/metadata/schema/testSecurityPageBinder.access.xml",
                "net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                .bind()
                .get(new PageContext("securityExtAttrMapperTest"), null));
    }

    @Test
    void unauthorizedExceptionTest() {
        try {
            compile("net/n2oapp/framework/access/metadata/schema/testSecurityPageBinder.access.xml",
                    "net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                    .bind()
                    .get(new PageContext("securityExtAttrMapperTest"), null);

            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    void accessDeniedExceptionTest() {
        when(permissionApi.hasAuthentication(any())).thenReturn(true);

        try {
            compile("net/n2oapp/framework/access/metadata/schema/testSecurityPageBinder.access.xml",
                    "net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                    .bind()
                    .get(new PageContext("securityExtAttrMapperTest"), null);

            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }
}
