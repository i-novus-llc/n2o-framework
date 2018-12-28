package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AnchorCompilerTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack());
        builder.ios(new AnchorElementIOV1());
        builder.compilers(new AnchorCompiler());
    }

    @Test
    public void testAnchor() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction.page.xml")
                .get(new PageContext("testAnchorAction"));
        LinkAction link1 = (LinkAction)page.getWidgets().get("testAnchorAction_test").getActions().get("id1");

        assertThat(link1.getOptions().getPath(), is("/test"));
        assertThat(link1.getOptions().getTarget(), is(Target.newWindow));

        LinkAction link2 = (LinkAction)page.getWidgets().get("testAnchorAction_test").getActions().get("id2");

        assertThat(link2.getOptions().getPath(), is("/test2"));
        assertThat(link2.getOptions().getTarget(), is(Target.application));
        PageRoutes.Route anchor = page.getRoutes().findRouteByUrl("/test2");
        assertThat(anchor.getIsOtherPage(), is(true));
    }
}
