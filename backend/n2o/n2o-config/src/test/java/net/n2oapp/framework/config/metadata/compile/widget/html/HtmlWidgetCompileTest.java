package net.n2oapp.framework.config.metadata.compile.widget.html;

import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.HtmlWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.PerformButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.toolbar.SubmenuCompiler;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.HtmlWidgetCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Тестирование компиляции html виджета
 */
public class HtmlWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.ios(new HtmlWidgetElementIOv4())
                .compilers(new HtmlWidgetCompiler(), new ToolbarCompiler(), new PerformButtonCompiler(), new SubmenuCompiler());
    }

    @Test
    public void testCompileActions() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testHtmlWidget4Compile.page.xml")
                .get(new PageContext("testHtmlWidget4Compile"));
        HtmlWidget htmlWidget = (HtmlWidget) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(htmlWidget.getId(), is("testHtmlWidget4Compile_w1"));
    }
}
