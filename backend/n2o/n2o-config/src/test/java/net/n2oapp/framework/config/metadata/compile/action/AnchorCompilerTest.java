package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.config.N2oApplicationBuilder;
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack())
        .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction2.page.xml"));
    }

    @Test
    public void testAnchor() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testAnchorAction.page.xml")
                .get(new PageContext("testAnchorAction"));
        LinkActionImpl link1 = (LinkActionImpl)page.getWidgets().get("page_test").getActions().get("id1");

        assertThat(link1.getUrl(), is("/test"));
        assertThat(link1.getTarget(), is(Target.application));
        assertThat(link1.getPathMapping().size(), is(0));
        assertThat(link1.getQueryMapping().size(), is(0));

        LinkActionImpl link2 = (LinkActionImpl)page.getWidgets().get("page_test").getActions().get("id2");

        assertThat(link2.getUrl(), is("/page/widget/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getTarget(), is(Target.application));
        assertThat(link2.getPathMapping().size(), is(2));
        assertThat(link2.getPathMapping().get("param1").getBindLink(), is("models.resolve['page_test']"));
        assertThat(link2.getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(link2.getPathMapping().get("param2").getBindLink(), is("models.resolve['page_test']"));
        assertThat(link2.getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(link2.getQueryMapping().size(), is(1));
        assertThat(link2.getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_test']"));
        assertThat(link2.getQueryMapping().get("param3").getValue(), is("`field3`"));
        PageRoutes.Route anchor = page.getRoutes().findRouteByUrl("/page/widget/test2/:param1/:param2?param3=:param3");
        assertThat(anchor.getIsOtherPage(), is(true));

        LinkActionImpl link3 = (LinkActionImpl)page.getWidgets().get("page_test").getActions().get("id3");
        assertThat(link3.getUrl(), is("http://google.com"));
        assertThat(link3.getTarget(), is(Target.self));

        PageContext modalContext = (PageContext) route("/page/widget/123/id4", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        link1 = (LinkActionImpl) modalPage.getWidget().getActions().get("id1");
        assertThat(link1.getUrl(), is("/page/widget/:page_test_id/id4/widget2/test"));
        assertThat(link1.getTarget(), is(Target.application));
        assertThat(link1.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(link1.getQueryMapping().size(), is(0));

        link2 = (LinkActionImpl) modalPage.getWidget().getActions().get("id2");
        assertThat(link2.getUrl(), is("/page/widget/:page_test_id/id4/widget2/test2/:param1/:param2?param3=:param3"));
        assertThat(link2.getTarget(), is(Target.application));
        assertThat(link2.getPathMapping().size(), is(3));
        assertThat(link2.getPathMapping().get("page_test_id").getBindLink(), is("models.resolve['page_test'].id"));
        assertThat(link2.getPathMapping().get("param1").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(link2.getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(link2.getPathMapping().get("param2").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(link2.getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(link2.getQueryMapping().size(), is(1));
        assertThat(link2.getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(link2.getQueryMapping().get("param3").getValue(), is("`field3`"));

        link3 = (LinkActionImpl) modalPage.getWidget().getActions().get("id3");
        assertThat(link3.getUrl(), is("/page/widget/test3"));
        assertThat(link3.getTarget(), is(Target.application));
        assertThat(link3.getPathMapping().size(), is(0));
        assertThat(link3.getQueryMapping().size(), is(0));

        LinkActionImpl linkSecond = (LinkActionImpl)page.getWidgets().get("page_secondWgt").getActions().get("secWgt");

        assertThat(linkSecond.getUrl(), is("/page/second/test/:minPrice"));
        assertThat(linkSecond.getTarget(), is(Target.newWindow));
        assertThat(linkSecond.getPathMapping().size(), is(1));
        assertThat(linkSecond.getPathMapping().get("minPrice").getBindLink(), is("models.filter['page_test']"));
        assertThat(linkSecond.getPathMapping().get("minPrice").getValue(), is("`minPrice`"));
    }
}
