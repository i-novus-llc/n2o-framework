package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.data.SecurityProvider;
import net.n2oapp.framework.access.exception.AccessDeniedException;
import net.n2oapp.framework.access.exception.UnauthorizedException;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.access.simple.PermissionApi;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.api.user.UserContext;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityPageBinderTest extends SourceCompileTestBase {

    private PermissionApi permissionApi;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        permissionApi = mock(PermissionApi.class);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);

        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                new N2oWidgetsPack(), new N2oRegionsPack(), new AccessSchemaPack())
                .extensions(new SecurityExtensionAttributeMapper());
    }

    @Test
    public void hasAuthenticationTest() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);
        when(permissionApi.hasRole(userContext, "admin")).thenReturn(true);

        Page page = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                .get(new PageContext("securityExtAttrMapperTest"));

        securityProvider.checkAccess(page, userContext);
    }

    @Test
    public void unauthorizedExceptionTest() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
//        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);

        Page page = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                .get(new PageContext("securityExtAttrMapperTest"));
        try {
            securityProvider.checkAccess(page, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnauthorizedException.class));
        }
    }

    @Test
    public void accessDeniedExceptionTest() {
        SecurityProvider securityProvider = new SecurityProvider(permissionApi);
        UserContext userContext = new UserContext(new TestContextEngine());
        when(permissionApi.hasAuthentication(userContext)).thenReturn(true);

        Page page = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                .get(new PageContext("securityExtAttrMapperTest"));

        try {
            securityProvider.checkAccess(page, userContext);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(AccessDeniedException.class));
        }
    }
}
