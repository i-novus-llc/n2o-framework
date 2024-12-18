package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.PrintType;
import net.n2oapp.framework.api.metadata.meta.action.print.PrintAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oActionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oRegionsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование компиляции действия печати
 */
class PrintActionCompileTest extends SourceCompileTestBase {
    
    @Override
    @BeforeEach
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
    void testPrint() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testPrintAction2.page.xml")
                .get(new PageContext("testPrintAction2"));
        Toolbar toolbar = ((Widget) page.getRegions().get("single").get(0).getContent().get(0)).getToolbar();
        PrintAction print = (PrintAction) toolbar.getButton("id1").getAction();

        assertThat(print.getPayload().getUrl(), is("/test"));
        assertThat(print.getPayload().getPathMapping().size(), is(0));
        assertThat(print.getPayload().getQueryMapping().size(), is(0));

        PrintAction print2 = (PrintAction) toolbar.getButton("id2").getAction();

        assertThat(print2.getPayload().getUrl(), is("/test2/:param1/:param2?param3=:param3"));
        assertThat(print2.getPayload().getPathMapping().size(), is(2));
        assertThat(print2.getPayload().getPathMapping().get("param1").getBindLink(), is("models.filter['page_secondWgt']"));
        assertThat(print2.getPayload().getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_test']"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(print2.getPayload().getQueryMapping().size(), is(1));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getBindLink(), is("models.filter['page_secondWgt']"));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getValue(), is("`field3`"));

        PrintAction print3 = (PrintAction) toolbar.getButton("id3").getAction();
        assertThat(print3.getPayload().getUrl(), is("http://google.com"));

        PrintAction print4 = (PrintAction) toolbar.getButton("id5").getAction();
        assertThat(print4.getPayload().getUrl(), is("/:url"));

        PrintAction linkSecond = (PrintAction) ((Widget) page.getRegions().get("single").get(0).getContent().get(1))
                .getToolbar().getButton("secWgt").getAction();

        assertThat(linkSecond.getPayload().getUrl(), is("/test/:minPrice"));
        assertThat(linkSecond.getPayload().getPathMapping().size(), is(1));
        assertThat(linkSecond.getPayload().getPathMapping().get("minPrice").getBindLink(), is("models.filter['page_test']"));
        assertThat(linkSecond.getPayload().getPathMapping().get("minPrice").getValue(), is("`minPrice`"));

        //Modal page
        PageContext modalContext = (PageContext) route("/page/id4", Page.class);
        SimplePage modalPage = (SimplePage) read().compile().get(modalContext);
        print = (PrintAction) modalPage.getWidget().getToolbar().getButton("id1").getAction();
        assertThat(print.getPayload().getUrl(), is("/page/id4/test"));
        assertThat(print.getPayload().getPathMapping().size(), is(0));
        assertThat(print.getPayload().getQueryMapping().size(), is(0));

        print2 = (PrintAction) modalPage.getWidget().getToolbar().getButton("id2").getAction();
        assertThat(print2.getPayload().getUrl(), is("/page/id4/test2/:param1/:param2?param3=:param3"));
        assertThat(print2.getPayload().getPathMapping().size(), is(2));
        assertThat(print2.getPayload().getPathMapping().get("param1").getBindLink(), is("models.resolve['page_id4_test']"));
        assertThat(print2.getPayload().getPathMapping().get("param1").getValue(), is("`field1`"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getBindLink(), is("models.resolve['page_id4_test']"));
        assertThat(print2.getPayload().getPathMapping().get("param2").getValue(), is("`field2`"));
        assertThat(print2.getPayload().getQueryMapping().size(), is(1));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getBindLink(), is("models.resolve['page_id4_test']"));
        assertThat(print2.getPayload().getQueryMapping().get("param3").getValue(), is("`field3`"));

        print3 = (PrintAction) modalPage.getWidget().getToolbar().getButton("id3").getAction();
        assertThat(print3.getPayload().getUrl(), is("/test3"));
        assertThat(print3.getPayload().getPathMapping().size(), is(0));
        assertThat(print3.getPayload().getQueryMapping().size(), is(0));
    }

    @Test
    void testPrintAttributes() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testPrintAction3.page.xml")
                .get(new PageContext("testPrintAction3"));
        Toolbar toolbar = page.getWidget().getToolbar();
        PrintAction print = (PrintAction) toolbar.getButton("id1").getAction();

        assertThat(print.getPayload().getUrl(), is("/test"));
        assertThat(print.getPayload().getDocumentTitle(), is("Document 1"));
        assertThat(print.getPayload().getLoader(), is(true));
        assertThat(print.getPayload().getLoaderText(), is("Loading..."));
        assertThat(print.getPayload().getKeepIndent(), is(false));
        assertThat(print.getPayload().getType(), is(PrintType.pdf));
        assertThat(print.getPayload().getBase64(), is(false));
    }
}
