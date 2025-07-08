package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.collection.Regions;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест ленивой загрузки вкладок региона <tabs>
 */
class TabsLazyInitAT extends AutoTestBase {

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
        builder.packs(
                new N2oAllPagesPack(),
                new N2oApplicationPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testLazyInit() {
        setResourcePath("net/n2oapp/framework/autotest/region/tabs/lazy");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("Ленивая загрузка вкладок региона tabs");
        FormWidget widget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        widget.fields().field("1").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("2").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("3").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("4").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("5").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("6").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("7").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("8").control(OutputText.class).shouldBeEmpty();
        widget.toolbar().topLeft().button("Данные").click();
        widget.fields().field("1").control(OutputText.class).shouldHaveValue("one");
        widget.fields().field("2").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("3").control(OutputText.class).shouldHaveValue("three");
        widget.fields().field("4").control(OutputText.class).shouldHaveValue("four");
        widget.fields().field("5").control(OutputText.class).shouldHaveValue("five");
        widget.fields().field("6").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("7").control(OutputText.class).shouldHaveValue("seven");
        widget.fields().field("8").control(OutputText.class).shouldHaveValue("eight");

        Regions regions = page.regions();
        regions.shouldHaveSize(5);

        checkTabRegion(regions.region(1, TabsRegion.class), "tab1", "one", "tab2", "two", "always-refresh=\"true\", lazy=\"true\"");
        checkTabRegion(regions.region(2, TabsRegion.class), "tab3", "three", "tab4", "four", "always-refresh=\"true\", lazy=\"false\"");
        checkTabRegion(regions.region(3, TabsRegion.class), "tab5", "five", "tab6", "six", "always-refresh=\"false\", lazy=\"true\"");
        checkTabRegion(regions.region(4, TabsRegion.class), "tab7", "seven", "tab8", "eight", "always-refresh=\"false\", lazy=\"false\"");
    }

    private void checkTabRegion(TabsRegion tab, String tab1Name, String tab1Value,
                                String tab2Name, String tab2Value,
                                String fieldLabel) {
        tab.shouldHaveSize(2);
        tab.tab(0).shouldHaveName(tab1Name);
        StandardField field = tab.tab(0).content().widget(FormWidget.class).fields().field(fieldLabel);
        field.control(InputText.class).shouldHaveValue(tab1Value);
        tab.tab(1).shouldHaveName(tab2Name);
        tab.tab(1).click();
        field = tab.tab(1).content().widget(FormWidget.class).fields().field(fieldLabel);
        field.control(InputText.class).shouldHaveValue(tab2Value);
    }


    @Test
    void testLazyInitWithDependencies() {
        setResourcePath("net/n2oapp/framework/autotest/region/tabs/laze_with_dependencies");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/laze_with_dependencies/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/laze_with_dependencies/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("Ленивая загрузка вкладок при наличие зависимостей");

        FormWidget widget = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        widget.fields().field("1").control(OutputText.class).shouldBeEmpty();
        widget.fields().field("2").control(OutputText.class).shouldBeEmpty();

        widget.toolbar().topLeft().button("Данные").click();
        widget.fields().field("1").control(OutputText.class).shouldHaveValue("test");
        widget.fields().field("2").control(OutputText.class).shouldBeEmpty();

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
    }
}
