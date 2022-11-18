package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MultiActionAT extends AutoTestBase {

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
    }


    @Test
    public void test() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/page.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);

        table.columns().rows().row(0).click();
        button.click();
        name.shouldHaveValue("test1");
        Selenide.back();

        table.columns().rows().row(3).click();
        button.click();
        name.shouldHaveValue("test4");
        Selenide.back();

        table.columns().rows().row(1).click();
        button.click();
        name.shouldHaveValue("test2");
        Selenide.back();

        table.columns().rows().row(2).click();
        button.click();
        name.shouldHaveValue("test3");
    }

    @Test
    public void testIfElse() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/if_else/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/if_else/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/if_else/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);

        table.columns().rows().row(0).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Меньше-равно 15");
        page.urlShouldMatches(getBaseUrl() + "/#/open1");
        name.shouldHaveValue("test1");
        Selenide.back();

        table.columns().rows().row(4).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 30 меньше-равно 60");
        page.urlShouldMatches(getBaseUrl() + "/#/open3");
        name.shouldHaveValue("test5");
        Selenide.back();

        table.columns().rows().row(3).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 60");
        page.urlShouldMatches(getBaseUrl() + "/#/open4");
        name.shouldHaveValue("test4");
        Selenide.back();

        table.columns().rows().row(5).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 15 меньше-равно 30");
        page.urlShouldMatches(getBaseUrl() + "/#/open2");
        name.shouldHaveValue("test6");
    }

    @Test
    public void testSwitchCaseInButton() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/button/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        TableWidget table = page.widget(TableWidget.class);
        Button button = table.toolbar().topLeft().button("Открыть");

        table.columns().rows().row(1).click();
        button.click();
        page.shouldExists();
        page.urlShouldMatches(getBaseUrl() + "/#/type1");
        Selenide.back();
        table.columns().rows().row(3).click();
        button.click();
        page.urlShouldMatches(getBaseUrl() + "/#/type2");
        Selenide.back();
        table.columns().rows().row(2).click();
        button.click();
        Assertions.assertEquals("https://example.com/", Selenide.webdriver().driver().url());
    }

    @Test
    public void testSwitchCaseRowClick() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/row_click/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/multi_action/switch_case/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        TableWidget table = page.widget(TableWidget.class);

        table.columns().rows().row(1).click();
        page.shouldExists();
        page.urlShouldMatches(getBaseUrl() + "/#/type1");
        Selenide.back();
        table.columns().rows().row(3).click();
        page.urlShouldMatches(getBaseUrl() + "/#/type2");
        Selenide.back();
        table.columns().rows().row(2).click();
        Assertions.assertEquals("https://example.com/", Selenide.webdriver().driver().url());
    }
}
