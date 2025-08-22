package net.n2oapp.framework.config.metadata.compile.widget.html;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.HtmlWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции html виджета
 */
class HtmlWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testCompileActions() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testHtmlWidget4Compile.page.xml")
                .get(new PageContext("testHtmlWidget4Compile"));
        HtmlWidget htmlWidget = (HtmlWidget) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(htmlWidget.getId(), is("testHtmlWidget4Compile_w1"));
    }

    @Test
    void testCompileContent() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testHtmlWidgetContentCompile.page.xml")
                .get(new PageContext("testHtmlWidgetContentCompile"));
        HtmlWidget htmlWidget = (HtmlWidget) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(htmlWidget.getHtml(), is("`'<div class=\\'test\\'><p>'+name+'</p></div>'`"));

        htmlWidget = (HtmlWidget) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(htmlWidget.getHtml(), is("<div class='test'><p>Hello</p></div>"));
    }
}
