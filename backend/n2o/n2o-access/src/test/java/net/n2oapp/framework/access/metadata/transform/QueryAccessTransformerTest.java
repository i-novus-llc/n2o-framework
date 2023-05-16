package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.QueryAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class QueryAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new AccessSchemaPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .transformers(new QueryAccessTransformer());
    }


    @Test
    void testQueryTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testQuery");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testQuery.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testQueryAccessTransformer.query.xml");

        CompiledQuery query = (CompiledQuery) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new QueryContext("testQueryAccessTransformer"));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions(), nullValue());
    }

    @Test
    void testQueryTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testQueryV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testQueryV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testQueryAccessTransformer.query.xml");

        CompiledQuery query = (CompiledQuery) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new QueryContext("testQueryAccessTransformer"));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions(), notNullValue());
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAnonymous(), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermitAll(), is(true));

        SecurityFilters securityFilters = (SecurityFilters) query.getProperties().get(SecurityFilters.SECURITY_FILTERS_PROP_NAME);
        //filters
        assertThat(securityFilters.getRoleFilters().size(), is(2));
        assertThat(securityFilters.getRoleFilters().get("role").size(), is(2));
        assertThat(securityFilters.getRoleFilters().get("role").get(0).getId(), is("idFilter"));
        assertThat(securityFilters.getRoleFilters().get("role").get(1).getId(), is("nameFilter"));
        assertThat(securityFilters.getRoleFilters().get("role2").size(), is(2));

        assertThat(securityFilters.getPermissionFilters().size(), is(2));
        assertThat(securityFilters.getPermissionFilters().get("permission").size(), is(2));
        assertThat(securityFilters.getPermissionFilters().get("permission").get(0).getId(), is("permIdFilter"));
        assertThat(securityFilters.getPermissionFilters().get("permission").get(1).getId(), is("permNameFilter"));
        assertThat(securityFilters.getPermissionFilters().get("permission2").size(), is(2));

        assertThat(securityFilters.getUserFilters().size(), is(1));
        assertThat(securityFilters.getUserFilters().get("user2").size(), is(2));
        assertThat(securityFilters.getUserFilters().get("user2").get(0).getId(), is("userIdFilter2"));
        assertThat(securityFilters.getUserFilters().get("user2").get(1).getId(), is("userNameFilter2"));

        assertThat(securityFilters.getAnonymousFilters().size(), is(2));
        assertThat(securityFilters.getAnonymousFilters().get(0).getId(), is("anonymIdFilter"));
        assertThat(securityFilters.getAnonymousFilters().get(1).getId(), is("anonymNameFilter"));

        assertThat(securityFilters.getPermitAllFilters().size(), is(2));
        assertThat(securityFilters.getPermitAllFilters().get(0).getId(), is("permitAllIdFilter"));
        assertThat(securityFilters.getPermitAllFilters().get(1).getId(), is("permitAllNameFilter"));

        assertThat(securityFilters.getAuthenticatedFilters().size(), is(2));
        assertThat(securityFilters.getAuthenticatedFilters().get(0).getId(), is("authIdFilter"));
        assertThat(securityFilters.getAuthenticatedFilters().get(1).getId(), is("authNameFilter"));

        //remove filters
        assertThat(securityFilters.getRemoveRoleFilters().size(), is(2));
        assertThat(securityFilters.getRemoveRoleFilters().get("role").size(), is(2));
        assertThat(securityFilters.getRemoveRoleFilters().get("role").contains("idFilter"), is(true));
        assertThat(securityFilters.getRemoveRoleFilters().get("role").contains("nameFilter"), is(true));
        assertThat(securityFilters.getRemoveRoleFilters().get("role2").size(), is(2));

        assertThat(securityFilters.getRemovePermissionFilters().size(), is(2));
        assertThat(securityFilters.getRemovePermissionFilters().get("permission").size(), is(2));
        assertThat(securityFilters.getRemovePermissionFilters().get("permission").contains("permIdFilter"), is(true));
        assertThat(securityFilters.getRemovePermissionFilters().get("permission").contains("permNameFilter"), is(true));
        assertThat(securityFilters.getRemovePermissionFilters().get("permission2").size(), is(2));

        assertThat(securityFilters.getRemoveUserFilters().size(), is(1));
        assertThat(securityFilters.getRemoveUserFilters().get("user2").size(), is(2));
        assertThat(securityFilters.getRemoveUserFilters().get("user2").contains("userIdFilter2"), is(true));
        assertThat(securityFilters.getRemoveUserFilters().get("user2").contains("userNameFilter2"), is(true));

        assertThat(securityFilters.getRemoveAnonymousFilters().size(), is(2));
        assertThat(securityFilters.getRemoveAnonymousFilters().contains("anonymIdFilter"), is(true));
        assertThat(securityFilters.getRemoveAnonymousFilters().contains("anonymNameFilter"), is(true));

        assertThat(securityFilters.getRemovePermitAllFilters().size(), is(2));
        assertThat(securityFilters.getRemovePermitAllFilters().contains("permitAllIdFilter"), is(true));
        assertThat(securityFilters.getRemovePermitAllFilters().contains("permitAllNameFilter"), is(true));

        assertThat(securityFilters.getRemoveAuthenticatedFilters().size(), is(2));
        assertThat(securityFilters.getRemoveAuthenticatedFilters().contains("authIdFilter"), is(true));
        assertThat(securityFilters.getRemoveAuthenticatedFilters().contains("authNameFilter"), is(true));

    }
}