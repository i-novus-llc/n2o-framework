package net.n2oapp.framework.autotest.region;

import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.N2oController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Автотест ленивой загрузки вкладок региона <tabs>
 */
public class TabsLazyInitAT extends AutoTestBase {

    @SpyBean
    private N2oController controller;
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
        setJsonPath("net/n2oapp/framework/autotest/region/tabs/lazy");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/region/tabs/lazy/test.query.xml"));
    }

    @Test
    public void testLazyInit() {
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

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab1");
        StandardField field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("one");
        tabs.tab(1).shouldHaveName("tab2");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("two");

        tabs = page.regions().region(2, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab3");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("three");
        tabs.tab(1).shouldHaveName("tab4");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"true\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("four");

        tabs = page.regions().region(3, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab5");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("five");
        tabs.tab(1).shouldHaveName("tab6");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"true\"");
        field.control(InputText.class).shouldHaveValue("six");

        tabs = page.regions().region(4, TabsRegion.class);
        tabs.shouldHaveSize(2);
        tabs.tab(0).shouldHaveName("tab7");
        field = tabs.tab(0).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("seven");
        tabs.tab(1).shouldHaveName("tab8");
        tabs.tab(1).click();
        field = tabs.tab(1).content().widget(FormWidget.class).fields().field("always-refresh=\"false\", lazy=\"false\"");
        field.control(InputText.class).shouldHaveValue("eight");
    }
}
