package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест ячейки с текстом
 */
public class TextCellAT extends AutoTestBase {
    private TableWidget.Rows rows;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/text/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(3);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    public void textCellTest() {
        TextCell cell1 = rows.row(0).cell(1);
        cell1.textShouldHave("test1");
        cell1.subTextShouldHave("1,1");
        TextCell cell2 = rows.row(0).cell(2);
        cell2.textShouldHave("1,23");

        cell1 = rows.row(1).cell(1);
        cell1.textShouldHave("test1test2test3");
        cell1.subTextShouldHave("");
        cell2 = rows.row(1).cell(2);
        cell2.textShouldHave("2,35");

        cell1 = rows.row(2).cell(1);
        cell1.textShouldHave("test3");
        cell1.subTextShouldHave("1,1", "2,2", "3,3");
        cell2 = rows.row(2).cell(2);
        cell2.textShouldHave("3,46");
    }
}
