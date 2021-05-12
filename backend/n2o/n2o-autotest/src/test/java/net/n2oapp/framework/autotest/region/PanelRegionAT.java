package net.n2oapp.framework.autotest.region;


import net.n2oapp.framework.autotest.Colors;
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
 * Автотест для региона в виде панели
 */
public class PanelRegionAT extends AutoTestBase {

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
    public void testPanelRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/panel/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        PanelRegion panel1 = page.regions().region(0, PanelRegion.class);
        panel1.shouldExists();
        panel1.shouldHaveTitle("Panel1");
        panel1.shouldHaveCssClass("css-on-panel");
        panel1.shouldHaveStyle("width: 90%;");
        panel1.shouldHaveBorderColor(Colors.DANGER);
        panel1.shouldHaveIcon("fa-exclamation");
        panel1.shouldBeCollapsible();
        panel1.shouldBeCollapsed();
        panel1.expandContent();
        panel1.shouldBeExpanded();
        panel1.shouldHaveFooterTitle("Footer");
        panel1.collapseContent();
        panel1.shouldBeCollapsed();

        // not collapsible panel
        PanelRegion panel2 = page.regions().region(1, PanelRegion.class);
        panel2.shouldExists();
        panel2.shouldHaveTitle("Panel2");
        panel2.shouldNotBeCollapsible();

        // panel without title
        PanelRegion panel3 = page.regions().region(2, PanelRegion.class);
        panel3.shouldExists();
        panel3.shouldNotHaveTitle();
        panel3.shouldNotBeCollapsible();
    }

    @Test
    public void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/panel/nesting/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        PanelRegion panelRegion = page.regions().region(0, PanelRegion.class);
        RegionItems content = panelRegion.content();

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
        panel.collapseContent();
        line.collapseContent();
        tabs.tab(1).click();

        panelRegion.collapseContent();
        panelRegion.shouldBeCollapsed();
        panelRegion.expandContent();

        panel.shouldBeCollapsed();
        line.shouldBeCollapsed();
        tabs.tab(1).shouldBeActive();
    }
}
