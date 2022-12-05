package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование действия if-else
 */
public class IfElseActionAT extends AutoTestBase {

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
    public void testIfElse() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/if_else/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/if_else/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/if_else/test.query.xml"));

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
        page.breadcrumb().crumb(0).click();

        table.columns().rows().row(4).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 30 меньше-равно 60");
        page.urlShouldMatches(getBaseUrl() + "/#/open3");
        name.shouldHaveValue("test5");
        page.breadcrumb().crumb(0).click();

        table.columns().rows().row(3).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 60");
        page.urlShouldMatches(getBaseUrl() + "/#/open4");
        name.shouldHaveValue("test4");
        page.breadcrumb().crumb(0).click();

        table.columns().rows().row(5).click();
        button.click();
        page.breadcrumb().crumb(1).shouldHaveLabel("Больше 15 меньше-равно 30");
        page.urlShouldMatches(getBaseUrl() + "/#/open2");
        name.shouldHaveValue("test6");
    }
}