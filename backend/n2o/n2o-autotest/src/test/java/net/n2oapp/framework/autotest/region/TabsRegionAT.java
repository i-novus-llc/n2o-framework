package net.n2oapp.framework.autotest.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
 * Автотест для региона в виде вкладок
 */
public class TabsRegionAT extends AutoTestBase {
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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testTabsRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.shouldHaveSize(3);
        tabs.shouldHaveCssClass("css-on-tabs");
        tabs.shouldHaveStyle("width: 90%;");
        tabs.tab(0).shouldBeActive();
        tabs.tab(1).shouldNotBeActive();
        tabs.tab(2).shouldNotBeActive();
        tabs.tab(0).shouldHaveName("Tab1");
        tabs.tab(1).shouldHaveName("Tab2");
        tabs.tab(2).shouldNotHaveTitle();

        tabs.tab(1).click();
        tabs.tab(0).shouldNotBeActive();
        tabs.tab(1).shouldBeActive();
        tabs.tab(2).shouldNotBeActive();

        tabs.tab(2).click();
        tabs.tab(0).shouldNotBeActive();
        tabs.tab(1).shouldNotBeActive();
        tabs.tab(2).shouldBeActive();

        TabsRegion tabs2 = page.regions().region(1, TabsRegion.class);
        // hiding single tab
        tabs2.shouldHaveSize(0);

        TabsRegion tabs3 = page.regions().region(2, TabsRegion.class);
        tabs3.shouldHaveSize(1);
        tabs3.tab(0).shouldHaveName("SingleTab");
        tabs3.tab(0).content().widget(FormWidget.class).shouldExists();
    }

    @Test
    public void testContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/nesting/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TabsRegion tabsRegion = page.regions().region(0, TabsRegion.class);
        RegionItems content = tabsRegion.tab(0).content();

        content.widget(0, FormWidget.class).fields().field("field1").shouldExists();

        SimpleRegion custom = content.region(1, SimpleRegion.class);
        custom.shouldExists();
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
        // after switch between tabs in global region
        panel.collapse();
        line.collapse();
        tabs.tab(1).click();

        tabsRegion.tab(1).click();
        tabsRegion.tab(1).shouldBeActive();
        tabsRegion.tab(0).click();

        panel.shouldBeCollapsed();
        line.shouldBeCollapsed();
        tabs.tab(1).shouldBeActive();
    }

    @Test
    public void testFixedContent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/fixed/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.shouldHaveSize(1);
        tabs.shouldHaveMaxHeight(200);
        tabs.shouldHaveScrollbar();
        TabsRegion.TabItem tab = tabs.tab(0);
        tab.scrollDown();
        Fields fields = tab.content().widget(FormWidget.class).fields();
        fields.field("input3").shouldExists();
        tab.scrollUp();
        fields.field("input1").shouldExists();

        tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(1);
        tabs.shouldNotHaveScrollbar();
    }

    @Test
    public void testVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/visible/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();


        RadioGroup radio = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields()
                .field("radio").control(RadioGroup.class);

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
        TabsRegion.TabItem tab1 = tabs.tab(Condition.text("One"));
        tab1.shouldBeActive();
        TabsRegion.TabItem tab4 = tabs.tab(Condition.text("Four"));

        // make visible tab3
        radio.check("visible tab3");
        tabs.shouldHaveSize(3);
        TabsRegion.TabItem tab3 = tabs.tab(Condition.text("Three"));
        tab3.click();
        tab3.shouldBeActive();
        InputText field3 = tab3.content().widget(FormWidget.class).fields().field("field3").control(InputText.class);
        field3.shouldHaveValue("test3");

        // make invisible tab3
        radio.check("invisible tab3");
        tabs.shouldHaveSize(2);
    }
}
