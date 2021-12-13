package net.n2oapp.framework.config.metadata.compile.widget.html;

import net.n2oapp.framework.api.metadata.meta.widget.HtmlWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.HtmlWidgetElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.toolbar.PerformButtonCompiler;
import net.n2oapp.framework.config.metadata.compile.toolbar.SubmenuCompiler;
import net.n2oapp.framework.config.metadata.compile.toolbar.ToolbarCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.HtmlWidgetCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
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
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.ios(new HtmlWidgetElementIOv4())
                .compilers(new HtmlWidgetCompiler(), new ToolbarCompiler(), new PerformButtonCompiler(), new SubmenuCompiler());
    }

    @Test
    public void testCompileActions() {
        HtmlWidget htmlWidget = (HtmlWidget) compile("net/n2oapp/framework/config/metadata/compile/widgets/testHtmlWidget4Compile.widget.xml")
                .get(new WidgetContext("testHtmlWidget4Compile"));
        assertThat(htmlWidget.getId(), is("$testHtmlWidget4Compile"));
    }
}
