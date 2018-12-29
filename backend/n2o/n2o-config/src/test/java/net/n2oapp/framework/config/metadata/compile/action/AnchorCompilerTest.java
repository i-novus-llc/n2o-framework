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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AnchorCompilerTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack())
        .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction2.page.xml"));
    }

    @Test
    public void testAnchor() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction.page.xml")
                .get(new PageContext("testAnchorAction"));
        LinkAction link1 = (LinkAction)page.getWidgets().get("page_test").getActions().get("id1");

        assertThat(link1.getOptions().getPath(), is("/test"));
        assertThat(link1.getOptions().getTarget(), is(Target.application));
        assertThat(link1.getOptions().getPathMapping().size(), is(0));
        assertThat(link1.getOptions().getQueryMapping().size(), is(0));

        LinkAction link2 = (LinkAction)page.getWidgets().get("page_test").getActions().get("id2");

        assertThat(link2.getOptions().getPath(), is("/page/widget/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getOptions().getTarget(), is(Target.application));
        assertThat(link2.getOptions().getPathMapping().size(), is(2));
        assertThat(link2.getOptions().getPathMapping().get("param1").getBindLink(), is("models.resolve['page_test'].field1"));
        assertThat(link2.getOptions().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_test'].field2"));
        assertThat(link2.getOptions().getQueryMapping().size(), is(1));
        assertThat(link2.getOptions().getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_test'].field3"));
        PageRoutes.Route anchor = page.getRoutes().findRouteByUrl("/page/widget/test2/:param1/:param2?param3=:param3");
        assertThat(anchor.getIsOtherPage(), is(true));

        LinkAction link3 = (LinkAction)page.getWidgets().get("page_test").getActions().get("id3");
        assertThat(link3.getOptions().getPath(), is("http://google.com"));
        assertThat(link3.getOptions().getTarget(), is(Target.self));

        PageContext modalContext = (PageContext) route("/page/widget/123/id4").getContext(Page.class);
        Page modalPage = read().compile().get(modalContext);
        link1 = (LinkAction)modalPage.getWidgets().get("page_widget_id4_test").getActions().get("id1");
        assertThat(link1.getOptions().getPath(), is("/page/widget/:page_test_id/id4/widget2/test"));
        assertThat(link1.getOptions().getTarget(), is(Target.application));
        assertThat(link1.getOptions().getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(link1.getOptions().getQueryMapping().size(), is(0));

        link2 = (LinkAction)modalPage.getWidgets().get("page_widget_id4_test").getActions().get("id2");
        assertThat(link2.getOptions().getPath(), is("/page/widget/:page_test_id/id4/widget2/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getOptions().getTarget(), is(Target.application));
        assertThat(link2.getOptions().getPathMapping().size(), is(3));
        assertThat(link2.getOptions().getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(link2.getOptions().getPathMapping().get("param1").getBindLink(), is("models.resolve['page_widget_id4_test'].field1"));
        assertThat(link2.getOptions().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_widget_id4_test'].field2"));
        assertThat(link2.getOptions().getQueryMapping().size(), is(1));
        assertThat(link2.getOptions().getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_widget_id4_test'].field3"));

        link3 = (LinkAction)modalPage.getWidgets().get("page_widget_id4_test").getActions().get("id3");
        assertThat(link3.getOptions().getPath(), is("/page/widget/test3"));
        assertThat(link3.getOptions().getTarget(), is(Target.application));
        assertThat(link3.getOptions().getPathMapping().size(), is(0));
        assertThat(link3.getOptions().getQueryMapping().size(), is(0));
    }
}
