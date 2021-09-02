package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.print.PrintAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции действия печати
 */
public class PrintActionCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testPrintAction.page.xml"));
    }

    @Test
    public void testPrint() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testPrintAction2.page.xml")
                .get(new PageContext("testPrintAction2"));
        Map actions = ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getActions();
        PrintAction print = (PrintAction) actions.get("id1");

        assertThat(print.getPayload().getUrl(), is("/test"));
        assertThat(print.getPayload().getPathMapping().size(), is(0));
        assertThat(print.getPayload().getQueryMapping().size(), is(0));

        PrintAction print2 = (PrintAction) actions.get("id2");

        assertThat(print2.getPayload().getUrl(), is("/page/widget/test2/:param1/:param2?param3=:param3"));
        assertThat(print2.getPayload().getPathMapping().size(), is(2));
        assertThat(print2.getPayload().getPathMapping().get("param1").getBindLink(), is("models.filter['page_ds2']"));
        assertThat(print2.getPayload().getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_ds1']"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(print2.getPayload().getQueryMapping().size(), is(1));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getBindLink(), is("models.filter['page_ds2']"));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getValue(), is("`field3`"));
        PageRoutes.Route anchor = page.getRoutes().findRouteByUrl("/page/widget/test2/:param1/:param2?param3=:param3");
        assertThat(anchor.getIsOtherPage(), is(true));

        PrintAction print3 = (PrintAction) actions.get("id3");
        assertThat(print3.getPayload().getUrl(), is("http://google.com"));

        PageContext modalContext = (PageContext) route("/page/widget/id4", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        print = (PrintAction) modalPage.getWidget().getActions().get("id1");
        assertThat(print.getPayload().getUrl(), is("/page/widget/id4/widget2/test"));
        assertThat(print.getPayload().getPathMapping().size(), is(0));
        assertThat(print.getPayload().getQueryMapping().size(), is(0));

        print2 = (PrintAction) modalPage.getWidget().getActions().get("id2");
        assertThat(print2.getPayload().getUrl(), is("/page/widget/id4/widget2/test2/:param1/:param2?param3=:param3"));
        assertThat(print2.getPayload().getPathMapping().size(), is(2));
        assertThat(print2.getPayload().getPathMapping().get("param1").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(print2.getPayload().getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(print2.getPayload().getQueryMapping().size(), is(1));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_widget_id4_test']"));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getValue(), is("`field3`"));

        print3 = (PrintAction) modalPage.getWidget().getActions().get("id3");
        assertThat(print3.getPayload().getUrl(), is("/page/widget/test3"));
        assertThat(print3.getPayload().getPathMapping().size(), is(0));
        assertThat(print3.getPayload().getQueryMapping().size(), is(0));

        PrintAction linkSecond = (PrintAction) ((Widget) page.getRegions().get("single").get(0).getContent().get(1))
                .getActions().get("secWgt");

        assertThat(linkSecond.getPayload().getUrl(), is("/page/second/test/:minPrice"));
        assertThat(linkSecond.getPayload().getPathMapping().size(), is(1));
        assertThat(linkSecond.getPayload().getPathMapping().get("minPrice").getBindLink(), is("models.filter['page_ds1']"));
        assertThat(linkSecond.getPayload().getPathMapping().get("minPrice").getValue(), is("`minPrice`"));
    }
}
