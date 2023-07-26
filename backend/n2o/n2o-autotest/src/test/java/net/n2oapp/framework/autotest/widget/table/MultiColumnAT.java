package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableFilterHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableMultiHeader;
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
 * Автотест для мульти-столбца таблицы
 */
public class MultiColumnAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());

    }

    @Test
    public void testMultiColumn() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/multi_column/simple");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/simple/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(7);
        rows.shouldHaveSize(4);

        TableSimpleHeader header1 = headers.header(0);
        TableMultiHeader header2 = headers.header(1, TableMultiHeader.class);
        TableSimpleHeader header3 = headers.header(2);
        TableMultiHeader header4 = headers.header(3, TableMultiHeader.class);
        TableFilterHeader header5 = headers.header(4, TableFilterHeader.class);
        TableSimpleHeader header6 = headers.header(5);
        TableSimpleHeader header7 = headers.header(6);
        // проверка порядка столбцов
        // столбцы первого уровня
        header1.shouldHaveTitle("ID");
        header2.shouldHaveTitle("Info");
        header2.shouldHaveCssClass("font-italic");
        header3.shouldHaveTitle("Birthday");
        // столбцы второго уровня
        header4.shouldHaveTitle("Name");
        header4.shouldHaveCssClass("font-italic");
        header5.shouldHaveTitle("Phone");
        // столбцы третьего уровня
        header6.shouldHaveTitle("FirstName");
        header6.shouldHaveCssClass("font-italic");
        header7.shouldHaveTitle("LastName");

        // проверка, что все ячейки корректно заполнены
        rows.row(0).cell(0).shouldHaveText("1");
        rows.row(0).cell(1).shouldHaveText("John");
        rows.row(0).cell(2).shouldHaveText("Smith");
        rows.row(0).cell(3).shouldHaveText("11-11-11");
        rows.row(0).cell(4).shouldHaveText("2018.12.31");

        // проверка работы фильтруемого столбца внутри мульти-столбца
        header5.openFilterDropdown();
        header5.filterControl(InputText.class).click();
        header5.filterControl(InputText.class).setValue("2");
        header5.clickSearchButton();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0).shouldHaveText("2");
        header5.openFilterDropdown();
        header5.clickResetButton();
        rows.shouldHaveSize(4);
    }

    @Test
    void testVisibilityDependencies() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/multi_column/visibility");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/visibility/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/multi_column/visibility/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.shouldExists();

        InputText inputText= form.fields().field("in1").control(InputText.class);
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Полное имя");
        table.columns().headers().header(1).shouldHaveTitle("Фамилия");
        table.columns().rows().row(0).cell(0).shouldHaveText("Smith");

        inputText.setValue("5");
        table.columns().headers().shouldHaveSize(3);
        table.columns().headers().header(0).shouldHaveTitle("Полное имя");
        table.columns().headers().header(1).shouldHaveTitle("Имя");
        table.columns().headers().header(2).shouldHaveTitle("Фамилия");
        table.columns().rows().row(0).cell(0).shouldHaveText("John");

    }
}
