package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ToolbarAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack(), new AccessSchemaPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.object.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .transformers(new ToolbarAccessTransformer(), new InvokeActionAccessTransformer());
    }

    @Test
    public void testToolbarTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbar");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testToolbar.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml");
        Page page = pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        Security.SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        Security.SecurityObject securityObjectAction = ((Security) page.getActions().get("create").getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().get(0), is("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().get(0), is("user"));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles().get(0), is("admin"));

        securityObjectToolbar = ((Security) page.getWidgets().get("testToolbarAccessTransformer_test").getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        securityObjectAction = ((Security) ((Action) page.getWidgets().get("testToolbarAccessTransformer_test").getActions()
                .get("update")).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().get(0), is("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().get(0), is("user"));
        assertThat(securityObjectToolbar.getRoles(), nullValue());

    }

    @Test
    public void testToolbarTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbarV2");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testToolbarV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml");
        Page page = pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        Security.SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        Security.SecurityObject securityObjectAction = ((Security) page.getActions().get("create").getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().get(0), is("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().get(0), is("user"));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles().get(0), is("admin"));
        assertThat(securityObjectToolbar.getAnonymous(), is(true));

        securityObjectToolbar = ((Security) page.getWidgets().get("testToolbarAccessTransformer_test").getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        securityObjectAction = ((Security) ((Action) page.getWidgets().get("testToolbarAccessTransformer_test").getActions()
                .get("update")).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), nullValue());
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().get(0), is("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().get(0), is("user"));
        assertThat(securityObjectToolbar.getRoles(), nullValue());
        assertThat(securityObjectToolbar.getAnonymous(), nullValue());
    }
}
