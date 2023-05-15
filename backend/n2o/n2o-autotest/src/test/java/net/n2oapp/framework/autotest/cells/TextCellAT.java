package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.api.metadata.meta.badge.Position;
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

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        setJsonPath("net/n2oapp/framework/autotest/cells/text");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/text/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/text/test.query.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    public void textCellTest() {
        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        TableWidget.Rows rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(3);

        TextCell cell1 = rows.row(0).cell(1);
        cell1.shouldHaveText("test1 1,1");
        cell1.shouldHaveSubText("1,1");
        cell1.shouldHaveIcon("fa-plus");
        cell1.shouldHaveIconPosition(Position.RIGHT);

        TextCell cell2 = rows.row(0).cell(2);
        cell2.shouldHaveText("1,23");
        cell2.shouldHaveIcon("fa-plus");
        cell2.shouldHaveIconPosition(Position.LEFT);

        cell1 = rows.row(1).cell(1);
        cell1.shouldHaveText("test1test2test3");
        cell1.shouldHaveSubText("");
        cell2 = rows.row(1).cell(2);
        cell2.shouldHaveText("2,35");

        cell1 = rows.row(2).cell(1);
        cell1.shouldHaveText("test3 1,1 2,2 3,3");
        cell1.shouldHaveSubText("1,1", "2,2", "3,3");
        cell2 = rows.row(2).cell(2);
        cell2.shouldHaveText("3,46");
    }
}
