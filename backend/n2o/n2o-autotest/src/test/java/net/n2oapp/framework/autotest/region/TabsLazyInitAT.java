package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
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
public class TabsLazyInitAT extends AutoTestBase {
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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/test.query.xml"));
    }

    @Test
    public void testLazyInit() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().titleShouldHaveText("Ленивая загрузка вкладок региона <tabs>");

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab2");
        StandardField field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("two");
        tabs.tab(1).shouldHaveName("tab3");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("three");

        tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab4");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("four");
        tabs.tab(1).shouldHaveName("tab5");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("five");

        tabs = page.regions().region(2, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab6");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("six");
        tabs.tab(1).shouldHaveName("tab7");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("seven");

        tabs = page.regions().region(3, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab8");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("eight");
        tabs.tab(1).shouldHaveName("tab9");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("nine");
    }

}
