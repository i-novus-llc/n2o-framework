package net.n2oapp.framework.autotest.datasources.cached;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
 * Тестирование cached-datasource
 */
public class CachedDatasourceAT extends AutoTestBase {

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
    }

    /**
     * Тестирование простого кейса
     */
    @Test
    void testSimple() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/cached_datasource/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/simple/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        InputText input = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Имя").control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue("test1");
        input.click();
        input.setValue("new value");

        Selenide.sleep(500);
        Selenide.refresh();
        input.shouldHaveValue("new value");

        Selenide.sessionStorage().clear();
        Selenide.refresh();

        page.shouldExists();
        input.shouldExists();
        input.shouldHaveValue("test1");

        Selenide.sessionStorage().clear();
    }

    /**
     * Тестирование кейса с параметрами, от которых зависит кэш
     */
    @Test
    void testPathParams() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/list.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/person.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        StandardButton button = table.toolbar().bottomLeft().button("Открыть");
        button.click();
        checkOpenPage("Opened Name 1");

        table.columns().rows().row(1).click();
        button.click();
        checkOpenPage("Opened Name 2");

        Selenide.sessionStorage().clear();
    }

    private void checkOpenPage(String name) {
        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.shouldExists();

        InputText input = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Имя").control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue(name);
        input.click();
        input.setValue("new value");

        Selenide.sleep(500);
        Selenide.refresh();
        input.shouldHaveValue("new value");

        open.breadcrumb().crumb(0).click();
    }
}
