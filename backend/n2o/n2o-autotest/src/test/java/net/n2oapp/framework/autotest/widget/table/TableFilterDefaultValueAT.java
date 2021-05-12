package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для проверки значений по умолчанию фильтров таблицы
 */
public class TableFilterDefaultValueAT extends AutoTestBase {
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/test.query.xml"));
    }

    @Test
    public void testSimple() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/default_value/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText filter = table.filters().fields().field("name").control(InputText.class);
        TableWidget.Rows rows = table.columns().rows();

        filter.shouldHaveValue("2");
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).textShouldHave("test2");

        filter.val("4");
        table.filters().toolbar().button("Найти").click();
        rows.row(0).cell(1).textShouldHave("test4");

        // filter value should stay after refresh
        Selenide.refresh();
        rows.row(0).cell(1).textShouldHave("test4");

        table.filters().toolbar().button("Сбросить").click();
        rows.shouldHaveSize(4);
        filter.shouldBeEmpty();
    }

    @Test
    public void testDefaultValuesQueryId() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/default_values_query_id/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/default.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText filter = table.filters().fields().field("name").control(InputText.class);
        TableWidget.Rows rows = table.columns().rows();

        filter.shouldHaveValue("test3");
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).textShouldHave("test3");

        filter.val("4");
        table.filters().toolbar().button("Найти").click();
        rows.row(0).cell(1).textShouldHave("test4");

        // filter value should stay after refresh
        Selenide.refresh();
        rows.row(0).cell(1).textShouldHave("test4");

        table.filters().toolbar().button("Сбросить").click();
        rows.shouldHaveSize(4);
        filter.shouldBeEmpty();
    }

    @Test
    public void testPriority() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/priority/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/default.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText filter = table.filters().fields().field("name").control(InputText.class);
        TableWidget.Rows rows = table.columns().rows();

        filter.shouldHaveValue("test3");
        rows.shouldHaveSize(1);
        rows.row(0).cell(1).textShouldHave("test3");

        filter.val("4");
        table.filters().toolbar().button("Найти").click();
        rows.row(0).cell(1).textShouldHave("test4");

        // filter value should stay after refresh
        Selenide.refresh();
        rows.row(0).cell(1).textShouldHave("test4");

        table.filters().toolbar().button("Сбросить").click();
        rows.shouldHaveSize(4);
        filter.shouldBeEmpty();
    }
}

