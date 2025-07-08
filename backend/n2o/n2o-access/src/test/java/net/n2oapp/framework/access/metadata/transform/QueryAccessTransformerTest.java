package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.QueryAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
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

import java.util.List;
import java.util.Set;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class QueryAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
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
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getPermissions(), nullValue());
    }

    @Test
    void testQueryTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testQueryV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testQueryV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testQueryAccessTransformer.query.xml");

        CompiledQuery query = (CompiledQuery) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new QueryContext("testQueryAccessTransformer"));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getPermissions(), notNullValue());
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getAnonymous(), is(true));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("object").getPermitAll(), is(true));

        SecurityFilters securityFilters = (SecurityFilters) query.getProperties().get(SecurityFilters.SECURITY_FILTERS_PROP_NAME);
        //filters
        checkFilters(securityFilters);

        //remove filters
        checkRemoveFilters(securityFilters);
    }

    private static void checkRemoveFilters(SecurityFilters securityFilters) {
        assertThat(securityFilters.getRemoveRoleFilters().size(), is(2));
        checkSecurityFiltersSet(securityFilters.getRemoveRoleFilters().get("role"), "idFilter", "nameFilter");
        assertThat(securityFilters.getRemoveRoleFilters().get("role2").size(), is(2));

        assertThat(securityFilters.getRemovePermissionFilters().size(), is(2));
        checkSecurityFiltersSet(securityFilters.getRemovePermissionFilters().get("permission"), "permIdFilter", "permNameFilter");
        assertThat(securityFilters.getRemovePermissionFilters().get("permission2").size(), is(2));

        assertThat(securityFilters.getRemoveUserFilters().size(), is(1));
        checkSecurityFiltersSet(securityFilters.getRemoveUserFilters().get("user2"), "userIdFilter2", "userNameFilter2");
        checkSecurityFiltersSet(securityFilters.getRemoveAnonymousFilters(), "anonymIdFilter", "anonymNameFilter");
        checkSecurityFiltersSet(securityFilters.getRemovePermitAllFilters(), "permitAllIdFilter", "permitAllNameFilter");
        checkSecurityFiltersSet(securityFilters.getRemoveAuthenticatedFilters(), "authIdFilter", "authNameFilter");
    }

    private static void checkFilters(SecurityFilters securityFilters) {
        assertThat(securityFilters.getRoleFilters().size(), is(2));
        checkSecurityFiltersList(securityFilters.getRoleFilters().get("role"), "idFilter", "nameFilter");
        assertThat(securityFilters.getRoleFilters().get("role2").size(), is(2));

        assertThat(securityFilters.getPermissionFilters().size(), is(2));
        checkSecurityFiltersList(securityFilters.getPermissionFilters().get("permission"), "permIdFilter", "permNameFilter");
        assertThat(securityFilters.getPermissionFilters().get("permission2").size(), is(2));

        assertThat(securityFilters.getUserFilters().size(), is(1));
        checkSecurityFiltersList(securityFilters.getUserFilters().get("user2"), "userIdFilter2", "userNameFilter2");
        checkSecurityFiltersList(securityFilters.getAnonymousFilters(), "anonymIdFilter", "anonymNameFilter");
        checkSecurityFiltersList(securityFilters.getPermitAllFilters(), "permitAllIdFilter", "permitAllNameFilter");
        checkSecurityFiltersList(securityFilters.getAuthenticatedFilters(), "authIdFilter", "authNameFilter");
    }

    private static void checkSecurityFiltersList(List<N2oObjectFilter> securityFilters, String authIdFilter, String authNameFilter) {
        assertThat(securityFilters.size(), is(2));
        assertThat(securityFilters.get(0).getId(), is(authIdFilter));
        assertThat(securityFilters.get(1).getId(), is(authNameFilter));
    }

    private static void checkSecurityFiltersSet(Set<String> securityFilters, String authIdFilter, String authNameFilter) {
        assertThat(securityFilters.size(), is(2));
        assertThat(securityFilters.contains(authIdFilter), is(true));
        assertThat(securityFilters.contains(authNameFilter), is(true));
    }
}