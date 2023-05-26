package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ObjectAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ObjectAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oObjectsPack(), new AccessSchemaPack())
                .transformers(new ObjectAccessTransformer());
    }

    @Test
    void testObjectTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testObject");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testObject.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml");

        CompiledObject object = (CompiledObject) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ObjectContext("testObjectAccessTransformer"));

        CompiledObject.Operation create = object.getOperations().get("create");
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().size(), is(3));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test2"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().size(), is(1));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().contains("role"), is(true));

        CompiledObject.Operation update = object.getOperations().get("update");
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().size(), is(2));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().size(), is(1));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
    }

    @Test
    void testObjectTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testObjectV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testObjectV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml");

        CompiledObject object = (CompiledObject) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ObjectContext("testObjectAccessTransformer"));

        CompiledObject.Operation create = object.getOperations().get("create");
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().size(), is(3));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test2"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().size(), is(1));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAnonymous(), is(true));

        CompiledObject.Operation update = object.getOperations().get("update");
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().size(), is(2));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().size(), is(1));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAnonymous(), is(true));

        SecurityFilters updateFilters = (SecurityFilters) update.getProperties().get(SecurityFilters.SECURITY_FILTERS_PROP_NAME);
        assertThat(updateFilters.getRemoveUserFilters().size(), is(1));
        assertThat(updateFilters.getRemoveUserFilters().get("user").size(), is(1));
        assertThat(updateFilters.getRemoveUserFilters().get("user").contains("nameFilter"), is(true));
        assertThat(updateFilters.getPermitAllFilters().size(), is(2));
    }
}