package net.n2oapp.framework.autotest.region;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.*;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
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
 * Автотест для региона в виде вкладок
 */
class TabsRegionAT extends AutoTestBase {
    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());

    }

    @Test
    void testTabsRegion() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/simple/index.page.xml"));
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
    void testContent() {
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
    void testFixedContent() {
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
    void testVisibleByWidget() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/visible_by_widget/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radio = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields()
                .field("radio").control(RadioGroup.class);

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
        TabsRegion.TabItem tab1 = tabs.tab(Condition.text("One"));
        tab1.shouldBeActive();
        TabsRegion.TabItem tab4 = tabs.tab(Condition.text("Four"));
        tab4.shouldNotBeActive();

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
        tab1.shouldBeActive();
    }

    @Test
    void testWidgetWithVisibleDepend() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/visible_by_widget/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.regions().region(1, TabsRegion.class).tab(0).shouldBeActive();
        page.regions().region(1, TabsRegion.class).tab(1).shouldNotBeActive();
    }


    @Test
    void testSearchButtons() {
        setResourcePath("net/n2oapp/framework/autotest/region/tabs/search_buttons");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/search_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/search_buttons/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.regions().region(0, TabsRegion.class).tab(0).shouldBeActive();
        TableWidget table = page.regions().region(0, TabsRegion.class).tab(0).content().widget(TableWidget.class);
        InputText filter = table.filters().fields().field("name").control(InputText.class);

        filter.shouldHaveValue("2");
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(1).shouldHaveText("test2");

        filter.setValue("3");
        table.filters().toolbar().button("Найти").click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(1).shouldHaveText("test3");

        table.filters().toolbar().button("Сбросить").click();
        table.columns().rows().shouldHaveSize(4);

        filter.setValue("1");
        table.filters().toolbar().button("Найти").click();
        table.columns().rows().shouldHaveSize(1);
    }

    @Test
    void testVisible() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/visible/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radio = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields()
                .field("Вкладки").control(RadioGroup.class);

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(1);
        TabsRegion.TabItem tab1 = tabs.tab(Condition.text("Первая"));
        tab1.shouldBeVisible();
        tab1.shouldBeActive();
        TabsRegion.TabItem tab2 = tabs.tab(Condition.text("Вторая"));
        tab2.shouldBeHidden();
        TabsRegion.TabItem tab3 = tabs.tab(Condition.text("Третья"));
        tab3.shouldBeHidden();
        TabsRegion.TabItem tab4 = tabs.tab(Condition.text("Четвертая"));
        tab4.shouldBeHidden();

        radio.check("Первая");
        tab1.shouldBeVisible();
        tab1.shouldBeActive();
        tab2.shouldBeVisible();
        tab3.shouldBeHidden();
        tab4.shouldBeHidden();
        tab2.click();
        tab2.shouldBeActive();
        tab1.shouldNotBeActive();

        radio.check("Вторая");
        tab1.shouldBeVisible();
        tab1.shouldBeActive();
        tab2.shouldBeHidden();
        tab3.shouldBeHidden();
        tab4.shouldBeVisible();
    }

    @Test
    void testEnabled() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/region/tabs/enabled/index.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radio = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields()
                .field("Вкладки").control(RadioGroup.class);

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(4);
        TabsRegion.TabItem tab1 = tabs.tab(Condition.text("Первая"));
        tab1.shouldBeActive();
        tab1.shouldBeEnabled();
        TabsRegion.TabItem tab2 = tabs.tab(Condition.text("Вторая"));
        tab2.shouldBeDisabled();
        TabsRegion.TabItem tab3 = tabs.tab(Condition.text("Третья"));
        tab3.shouldBeDisabled();
        TabsRegion.TabItem tab4 = tabs.tab(Condition.text("Четвертая"));
        tab4.shouldBeDisabled();

        tab2.click();
        tab1.shouldBeActive();
        tab3.click();
        tab1.shouldBeActive();
        tab4.click();
        tab1.shouldBeActive();

        radio.check("Первая");
        tab1.shouldBeActive();
        tab2.shouldBeEnabled();
        tab3.shouldBeDisabled();
        tab4.shouldBeDisabled();
        tab2.click();
        tab2.shouldBeActive();
        tab1.shouldNotBeActive();

        radio.check("Вторая");
        tab1.shouldBeEnabled();
        tab1.shouldBeActive();
        tab2.shouldBeDisabled();
        tab3.shouldBeDisabled();
        tab4.shouldBeEnabled();
        tab4.click();
        tab4.shouldBeActive();
    }
}
