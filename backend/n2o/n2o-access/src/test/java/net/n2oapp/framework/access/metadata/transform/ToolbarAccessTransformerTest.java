package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.AbstractButton;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Submenu;
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

import java.util.List;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        Security.SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        Security.SecurityObject securityObjectAction = ((Security) page.getToolbar().getButton("create").getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().contains("permission"), is(true));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().contains("user"), is(true));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles().contains("admin"), is(true));

        Widget widget = (Widget) page.getRegions().get("topLeft").get(0).getContent().get(0);
        securityObjectToolbar = ((Security) widget.getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        securityObjectAction = ((Security) ((Action) widget.getToolbar()
                .getButton("update").getAction()).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions().contains("permission"), is(true));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames().contains("user"), is(true));
        assertThat(securityObjectToolbar.getRoles(), nullValue());
    }

    @Test
    public void testToolbarTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbarV2");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testToolbarV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml");
        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testToolbarAccessTransformer"));
        Security.SecurityObject securityObjectToolbar = ((Security) page.getToolbar().get("bottomRight").get(0)
                .getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        Security.SecurityObject securityObjectAction = ((Security) page.getToolbar().getButton("create").getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), is(true));
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions(), hasItem("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames(), hasItem("user"));
        assertThat(securityObjectToolbar.getRoles().size(), is(1));
        assertThat(securityObjectToolbar.getRoles(), hasItem("admin"));
        assertThat(securityObjectToolbar.getAnonymous(), is(true));

        Widget widget = (Widget) page.getRegions().get("topLeft").get(0).getContent().get(0);
        securityObjectToolbar = ((Security) widget.getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        securityObjectAction = ((Security) ((Action) widget.getToolbar()
                .getButton("update").getAction()).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object");
        assertThat(securityObjectAction.equals(securityObjectToolbar), is(true));
        assertThat(securityObjectAction.getAnonymous(), nullValue());
        assertThat(securityObjectToolbar.getPermissions().size(), is(1));
        assertThat(securityObjectToolbar.getPermissions(), hasItem("permission"));
        assertThat(securityObjectToolbar.getUsernames().size(), is(1));
        assertThat(securityObjectToolbar.getUsernames(), hasItem("user"));
        assertThat(securityObjectToolbar.getRoles(), nullValue());
        assertThat(securityObjectToolbar.getAnonymous(), nullValue());
    }

    @Test
    public void testSubMenu() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testSubMenu");

        ReadCompileTerminalPipeline<?> pipeline = compile("net/n2oapp/framework/access/metadata/schema/testSubMenu.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testSubMenuAccess.page.xml",
                "net/n2oapp/framework/access/metadata/transform/testSubMenuAccess.object.xml");

        StandardPage page = (StandardPage) pipeline.transform().get(new PageContext("testSubMenuAccess"));

        //permitAll в одном из menuItem делает доступным subMenu
        List<AbstractButton> buttons = ((Widget) page.getRegions().get("topRight").get(0).getContent().get(0))
                .getToolbar().get("topLeft").get(0).getButtons();
        AbstractButton subMenu1 = buttons.get(0);
        assertTrue(((Security) subMenu1.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermitAll());

        AbstractButton subMenu2 = buttons.get(1);
        assertTrue(((Security) subMenu2.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAnonymous());
        assertTrue(((Security) subMenu2.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAuthenticated());

        AbstractButton subMenu3 = buttons.get(2);
        assertTrue(((Security) subMenu2.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAuthenticated());
        assertFalse(((Security) subMenu3.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getAnonymous());
        assertFalse(((Security) subMenu3.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles().isEmpty());
        assertFalse(((Security) subMenu3.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().isEmpty());
        assertFalse(((Security) subMenu3.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions().isEmpty());

        //Если одна из кнопок не имеет security, то subMenu тоже не будет иметь security
        Submenu subMenu4 = (Submenu) buttons.get(3);
        assertThat(subMenu4.getSubMenu().get(0).getProperties().get(SECURITY_PROP_NAME), notNullValue());
//        assertThat(subMenu4.getSubMenu().get(1).getProperties().isEmpty(), is(true));
        assertThat(subMenu4.getProperties(), nullValue());

        //Если одна из кнопок не имеет security, то subMenu тоже не будет иметь security
        assertThat(subMenu4.getSubMenu().get(2).getProperties().get(SECURITY_PROP_NAME), notNullValue());
        assertThat(((Security) subMenu4.getSubMenu().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("custom"), notNullValue());
        assertThat(((Security) subMenu4.getSubMenu().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("custom").getPermissions(), hasItem("test"));
    }
}
