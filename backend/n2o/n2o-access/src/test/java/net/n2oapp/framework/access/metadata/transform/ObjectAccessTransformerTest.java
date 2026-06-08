package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ObjectAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityFilters;
import net.n2oapp.framework.access.metadata.SecurityObject;
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

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

class ObjectAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
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
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().size(), is(3));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test2"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getRoles().size(), is(1));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getRoles().contains("role"), is(true));

        CompiledObject.Operation update = object.getOperations().get("update");
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().size(), is(2));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().size(), is(1));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().contains("user"), is(true));
    }

    @Test
    void testObjectTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testObjectV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testObjectV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml");

        CompiledObject object = (CompiledObject) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ObjectContext("testObjectAccessTransformer"));

        CompiledObject.Operation create = object.getOperations().get("create");
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().size(), is(3));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test2"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getRoles().size(), is(1));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getRoles().contains("role"), is(true));
        assertThat(((Security) create.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getAnonymous(), is(true));

        CompiledObject.Operation update = object.getOperations().get("update");
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().size(), is(2));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions().contains("test3"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().size(), is(1));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().contains("user"), is(true));
        assertThat(((Security) update.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getAnonymous(), is(true));

        SecurityFilters updateFilters = (SecurityFilters) update.getProperties().get(SecurityFilters.SECURITY_FILTERS_PROP_NAME);
        assertThat(updateFilters.getRemoveUserFilters().size(), is(1));
        assertThat(updateFilters.getRemoveUserFilters().get("user").size(), is(1));
        assertThat(updateFilters.getRemoveUserFilters().get("user").contains("nameFilter"), is(true));
        assertThat(updateFilters.getPermitAllFilters().size(), is(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    void compilesNestedFieldSecurityWithSameIds() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties())
                .setProperty("n2o.access.schema.id", "testFieldSecurityNested");

        ReadCompileTerminalPipeline pipeline = compile(
                "net/n2oapp/framework/access/metadata/schema/testFieldSecurityNested.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testFieldSecurityNested.object.xml");

        CompiledObject object = (CompiledObject) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ObjectContext("testFieldSecurityNested"));
        CompiledObject.Operation update = object.getOperations().get("update");

        Map<String, Security> inSec =
                (Map<String, Security>) update.getProperties().get(Security.IN_FIELD_SECURITY_PROP_NAME);
        assertThat(inSec, notNullValue());
        assertThat(inSec.get("ref1.name").getFirst().get("custom").getRoles(), containsInAnyOrder("hr"));
        assertThat(inSec.get("ref1.list.name").getFirst().get("custom").getRoles(), containsInAnyOrder("admin"));
        assertThat(inSec.get("ref2.name").getFirst().get("custom").getRoles(), containsInAnyOrder("manager"));
        assertThat(inSec.containsKey("ref1"), is(false));
        assertThat(inSec.containsKey("ref2"), is(false));
        assertThat(inSec.containsKey("ref1.list"), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    void compilesInAndOutFieldSecurity() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties())
                .setProperty("n2o.access.schema.id", "testFieldSecurityObject");

        ReadCompileTerminalPipeline pipeline = compile(
                "net/n2oapp/framework/access/metadata/schema/testFieldSecurityObject.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testFieldSecurityOperation.object.xml");

        CompiledObject object = (CompiledObject) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ObjectContext("testFieldSecurityOperation"));
        CompiledObject.Operation update = object.getOperations().get("update");

        Map<String, Security> inSec =
                (Map<String, Security>) update.getProperties().get(Security.IN_FIELD_SECURITY_PROP_NAME);
        assertThat(inSec, notNullValue());
        SecurityObject salary = inSec.get("salary").getFirst().get("custom");
        assertThat(salary.getRoles(), containsInAnyOrder("hr", "admin"));
        assertThat(inSec.containsKey("name"), is(false));
        assertThat(inSec.containsKey("org"), is(false));

        SecurityObject user = inSec.get("user").getFirst().get("custom");
        assertThat(user.getRoles(), containsInAnyOrder("hr", "admin"));

        SecurityObject deps = inSec.get("deps").getFirst().get("custom");
        assertThat(deps.getRoles(), containsInAnyOrder("hr", "admin"));

        SecurityObject users = inSec.get("users").getFirst().get("custom");
        assertThat(users.getRoles(), containsInAnyOrder("hr", "admin"));

        Map<String, Security> outSec =
                (Map<String, Security>) update.getProperties().get(Security.OUT_FIELD_SECURITY_PROP_NAME);
        assertThat(outSec, notNullValue());
        SecurityObject secret = outSec.get("secret").getFirst().get("custom");
        assertThat(secret.getPermissions(), containsInAnyOrder("view-sensitive"));
        assertThat(outSec.containsKey("id"), is(false));
    }
}
