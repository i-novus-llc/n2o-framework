package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ToolbarAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
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
    void testToolbarTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbar");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testToolbar.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        SecurityObject securityObjectAction = ((Security) page.getToolbar().getButton("create").getProperties().get(SECURITY_PROP_NAME))
                .get(0).get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().contains("permission"), is(true));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().contains("user"), is(true));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles().contains("admin"), is(true));

        Widget widget = (Widget) page.getRegions().get("single").get(0).getContent().get(0);
        securityObjectToolbar = ((Security) widget.getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        securityObjectAction = ((Security) ((Action) widget.getToolbar()
                .getButton("update").getAction()).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().contains("permission"), is(true));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().contains("user"), is(true));
        assertThat(securityObjectToolbar.getRoles(), nullValue());
    }

    @Test
    void testToolbarTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbarV2");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testToolbarV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        SecurityObject securityObjectAction = ((Security) page.getToolbar().getButton("create").getProperties().get(SECURITY_PROP_NAME))
                .get(0).get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions(), hasItem("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames(), hasItem("user"));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles(), hasItem("admin"));
        assertThat(securityObjectToolbar.getAnonymous(), is(true));

        Widget widget = (Widget) page.getRegions().get("single").get(0).getContent().get(0);
        securityObjectToolbar = ((Security) widget.getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        securityObjectAction = ((Security) ((Action) widget.getToolbar()
                .getButton("update").getAction()).getProperties().get(SECURITY_PROP_NAME)).get(0).get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), nullValue());
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions(), hasItem("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames(), hasItem("user"));
        assertThat(securityObjectToolbar.getRoles(), nullValue());
        assertThat(securityObjectToolbar.getAnonymous(), nullValue());
    }
}
