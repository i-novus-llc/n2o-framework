package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.OpenPageAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenPageAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.object.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testOpenPageTransformerModal.page.xml"))
                .packs(new N2oAllPagesPack(), new AccessSchemaPack(), new N2oObjectsPack(), new N2oDataProvidersPack())
                .transformers(new ToolbarAccessTransformer(), new OpenPageAccessTransformer());
    }

    @Test
    void testOpenPageTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testOpenPage");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testOpenPage.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testOpenPageAccessTransformer.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testOpenPageAccessTransformer"));

        Map<String, SecurityObject> securityMap = ((Security) page.getToolbar().get("bottomRight")
                .get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).get(0);
        SecurityObject securityObject = securityMap.get("url");
        assertThat(securityObject.getPermissions().size(), is(1));
        assertTrue(securityObject.getPermissions().contains("permission"));

        securityObject = securityMap.get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertTrue(securityObject.getRoles().contains("admin"));

        List<AbstractButton> buttons = ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar().get("topLeft")
                .get(0).getButtons();
        securityObject = (((Security) buttons.get(0).getProperties().get(SECURITY_PROP_NAME))).get(0).get("url");
        assertThat(securityObject.getRoles().size(), is(1));
        assertTrue(securityObject.getRoles().contains("role"));
        assertThat(securityObject.getUsernames(), nullValue());

        securityObject = (((Security) buttons.get(0).getProperties().get(SECURITY_PROP_NAME))).get(0).get("page");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getPermissions(), nullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertTrue(securityObject.getRoles().contains("admin"));

        securityObject = (((Security) buttons.get(2).getProperties().get(SECURITY_PROP_NAME))).get(0).get("url");
        assertThat(securityObject.getUsernames(), nullValue());
        assertThat(securityObject.getRoles(), nullValue());
        assertThat(securityObject.getPermissions().size(), is(1));
        assertTrue(securityObject.getPermissions().contains("permission2"));
        assertTrue(securityObject.getAnonymous());
    }
}
