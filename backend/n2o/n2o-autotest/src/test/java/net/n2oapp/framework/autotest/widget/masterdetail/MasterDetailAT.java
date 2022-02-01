package net.n2oapp.framework.autotest.widget.masterdetail;

import com.codeborne.selenide.Configuration;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterDetailAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        Configuration.headless = false;
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/master_detail/detail.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/master_detail/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/master_detail/open.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/master_detail/test.query.xml"));
    }

    @Test
    public void testMasterDetail() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Master-detail фильтрация");

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);

        Cells row = table.columns().rows().row(1);
        row.cell(0).textShouldHave("2");
        row.cell(1).textShouldHave("test2");
        row.click();

        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.shouldExists();
        open.breadcrumb().titleShouldHaveText("Вторая страница");

        FormWidget form = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.shouldExists();
        form.fields().shouldHaveSize(2);
        form.fields().field("Идентификатор выбранной ранее записи").control(InputText.class).shouldHaveValue("2");
        form.fields().field("Имя выбранной ранее записи").control(InputText.class).shouldHaveValue("test2");

        table = open.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(1).shouldHaveTitle("Тип (должен совпадать с идентификатором выбранной ранее записи)");

        table.columns().rows().shouldHaveSize(2);
        TableWidget.Rows rows = table.columns().rows();
        rows.row(0).cell(0).textShouldHave("test33");
        rows.row(0).cell(1).textShouldHave("2");
        rows.row(1).cell(0).textShouldHave("test44");
        rows.row(1).cell(1).textShouldHave("2");
    }
}
