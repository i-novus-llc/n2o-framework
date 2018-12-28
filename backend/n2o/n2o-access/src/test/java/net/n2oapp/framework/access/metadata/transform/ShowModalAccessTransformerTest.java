package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.ShowModalAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.Page;
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

public class ShowModalAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.object.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"))
                .packs(new N2oAllDataPack(), new N2oAllPagesPack(), new AccessSchemaPack())
                .transformers(new ToolbarAccessTransformer(), new ShowModalAccessTransformer());
    }

    @Test
    public void testShowModal() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testShowModal");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testShowModal.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testShowModalAccessTransformer.page.xml");

        Page page = (Page) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new PageContext("testShowModalAccessTransformer"));

        Security.SecurityObject securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObject.getPermissions().size(), is(1));
        assertThat(securityObject.getPermissions().get(0), is("permission"));
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().get(0), is("admin"));
        assertThat(securityObject.getUsernames().size(), is(1));
        assertThat(securityObject.getUsernames().get(0), is("user"));

        securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("admin"), is(true));


        securityObject = ((Security) page.getWidgets().get("testShowModalAccessTransformer_widgetId").getToolbar().get("topLeft")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObject.getPermissions().size(), is(2));
        assertThat(securityObject.getPermissions().contains("permission"), is(true));
        assertThat(securityObject.getPermissions().contains("permission2"), is(true));
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().get(0), is("role"));
        assertThat(securityObject.getUsernames(), nullValue());

        securityObject = ((Security) page.getWidgets().get("testShowModalAccessTransformer_widgetId").getToolbar().get("topLeft")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("admin"), is(true));
    }

    @Test
    public void testShowModalV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testShowModalV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testShowModalV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testShowModalAccessTransformer.page.xml");

        Page page = (Page) ((ReadCompileTerminalPipeline) pipeline.transform()).get(new PageContext("testShowModalAccessTransformer"));

        Security.SecurityObject securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObject.getPermissions().size(), is(1));
        assertThat(securityObject.getPermissions().get(0), is("permission"));
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().get(0), is("admin"));
        assertThat(securityObject.getUsernames().size(), is(1));
        assertThat(securityObject.getUsernames().get(0), is("user"));
        assertThat(securityObject.getAnonymous(), nullValue());

        securityObject = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("admin"), is(true));
        assertThat(securityObject.getAnonymous(), nullValue());

        securityObject = ((Security) page.getWidgets().get("testShowModalAccessTransformer_widgetId").getToolbar().get("topLeft")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("object");
        assertThat(securityObject.getPermissions().size(), is(2));
        assertThat(securityObject.getPermissions().contains("permission"), is(true));
        assertThat(securityObject.getPermissions().contains("permission2"), is(true));
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().get(0), is("role"));
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getAnonymous(), is(true));

        securityObject = ((Security) page.getWidgets().get("testShowModalAccessTransformer_widgetId").getToolbar().get("topLeft")
                .get(0).getButtons().get(0).getProperties().get("security")).getSecurityMap().get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("admin"), is(true));
        assertThat(securityObject.getAnonymous(), nullValue());
    }
}
