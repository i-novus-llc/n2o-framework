package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.*;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для региона с горизонтальным делителем
 */
public class LineRegionAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oHeaderPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/simple/test.header.xml"));
    }

    @Test
    public void testLineRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/line/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        // expanded
        LineRegion line1 = page.regions().region(0, LineRegion.class);
        line1.shouldHaveLabel("Line1");
        line1.shouldHaveCssClass("css-on-line");
        line1.shouldHaveStyle("width: 90%;");
        line1.shouldBeCollapsible();
        line1.shouldBeExpanded();
        line1.collapse();
        line1.shouldBeCollapsed();
        line1.expand();
        line1.shouldBeExpanded();

        // collapsed
        LineRegion line2 = page.regions().region(1, LineRegion.class);
        line2.shouldHaveLabel("Line2");
        line2.shouldBeCollapsed();

        // not collapsible line
        LineRegion line3 = page.regions().region(2, LineRegion.class);
        line3.shouldNotBeCollapsible();
    }

    @Test
    public void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/line/nesting/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        LineRegion lineRegion = page.regions().region(0, LineRegion.class);
        RegionItems content = lineRegion.content();

        content.widget(0, FormWidget.class).fields().field("field1").shouldExists();

        SimpleRegion custom = content.region(1, SimpleRegion.class);
        custom.content().widget(FormWidget.class).fields().field("field2").shouldExists();

        PanelRegion panel = content.region(2, PanelRegion.class);
        panel.shouldExists();
        panel.shouldHaveTitle("Panel");

        LineRegion line = content.region(3, LineRegion.class);
        line.shouldExists();
        line.shouldHaveLabel("Line");

        TabsRegion tabs = content.region(4, TabsRegion.class);
        tabs.shouldExists();
        tabs.shouldHaveSize(2);
        tabs.tab(1).shouldHaveName("Tab2");

        content.widget(5, FormWidget.class).fields().field("field3").shouldExists();

        // testing collapse/expand state of nesting regions
        // after collapse->expand global region
        panel.collapse();
        line.collapse();
        tabs.tab(1).click();

        lineRegion.collapse();
        lineRegion.shouldBeCollapsed();
        lineRegion.expand();

        panel.shouldBeCollapsed();
        line.shouldBeCollapsed();
        tabs.tab(1).shouldBeActive();
    }
}
