package net.n2oapp.framework.autotest.widget.html;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.html.HtmlWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для виджета html
 */
public class HtmlWidgetAT extends AutoTestBase {
    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testHtmlContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/html/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.regions().region(0, SimpleRegion.class).content().widget(HtmlWidget.class).shouldHaveElement("div.test");
    }

    @Test
    public void testHtmlGetSrc() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/html/htmlFromSrc/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        page.regions().region(0, SimpleRegion.class).content().widget(HtmlWidget.class).shouldHaveElement("div.testFromURL");
    }
}
