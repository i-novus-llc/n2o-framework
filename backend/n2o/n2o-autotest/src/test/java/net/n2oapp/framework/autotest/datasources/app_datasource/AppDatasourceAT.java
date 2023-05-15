package net.n2oapp.framework.autotest.datasources.app_datasource;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.application.Sidebar;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
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
 * Тестирование app-datasource
 */
public class AppDatasourceAT extends AutoTestBase {

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

    @Test
    public void testAppDS() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/app_datasource/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/simple/side.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/simple/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/simple/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("App datasource. Ссылка на источник данных, объявленный в application.xml");

        Sidebar sidebar = page.sidebar();
        InputText id = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("id").control(InputText.class);
        InputText name = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("name").control(InputText.class);
        InputText step2 = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("step2").control(InputText.class);

        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
        sidebar.nav().anchor(1).click();
        step2.shouldHaveValue("test-step2");
        sidebar.nav().anchor(0).click();
        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
    }

    /**
     * Тестирование Прокидывание разных app-datasource из родительской страницы
     */
    @Test
    public void testSourceDatasource() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/app_datasource/source_datasource");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/source_datasource/default.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/source_datasource/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/source_datasource/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/app_datasource/source_datasource/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table1 = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, TableWidget.class);
        table1.shouldExists();
        StandardButton showModalBtn = table1.toolbar().topLeft().button("Открыть");
        showModalBtn.shouldExists();
        showModalBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();

        FormWidget formWidget = modalPage.content(StandardPage.class).regions().region(0, SimpleRegion.class).content()
                .widget(0,FormWidget.class);
        formWidget.fields().field("id").control(InputText.class).setValue("5");
        formWidget.fields().field("name").control(InputText.class).setValue("Mary");
        modalPage.toolbar().bottomRight().button("Сохранить").click();

        table1.columns().rows().shouldHaveSize(5);
        table1.columns().rows().row(4).cell(0).shouldHaveText("5");

        TableWidget table2 = page.regions().region(0, SimpleRegion.class).content()
                .widget(2, TableWidget.class);
        table1.shouldExists();
        showModalBtn = table2.toolbar().topLeft().button("Открыть");
        showModalBtn.shouldExists();
        showModalBtn.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldExists();

        formWidget = modalPage.content(StandardPage.class).regions().region(0, SimpleRegion.class).content()
                .widget(0,FormWidget.class);
        formWidget.fields().field("id").control(InputText.class).setValue("3");
        formWidget.fields().field("name").control(InputText.class).setValue("Mary");
        modalPage.toolbar().bottomRight().button("Сохранить").click();

        table2.columns().rows().shouldHaveSize(3);
        table2.columns().rows().row(2).cell(0).shouldHaveText("3");
    }
}
