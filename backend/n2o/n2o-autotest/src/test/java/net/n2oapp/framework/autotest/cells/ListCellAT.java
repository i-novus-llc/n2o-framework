package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.ListCell;
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
 * Автотест ячеек таблицы
 */
public class ListCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/list/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/testTable.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        rows = simplePage.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }

    @Test
    public void listCellTest() {
        int col = 0;
        rows.row(0).cell(col, ListCell.class).shouldHaveSize(1);
        rows.row(1).cell(col, ListCell.class).shouldHaveSize(3);
        rows.row(2).cell(col, ListCell.class).shouldHaveSize(0);
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(3);

        rows.row(0).cell(col, ListCell.class).shouldHaveCollapseExpand(false);
        rows.row(1).cell(col, ListCell.class).shouldHaveCollapseExpand(false);
        rows.row(2).cell(col, ListCell.class).shouldHaveCollapseExpand(false);
        rows.row(3).cell(col, ListCell.class).shouldHaveCollapseExpand(true);

        rows.row(0).cell(col, ListCell.class).shouldHaveText(0, "val1");
        rows.row(1).cell(col, ListCell.class).shouldHaveText(1, "val2");
        rows.row(3).cell(col, ListCell.class).shouldHaveText(2, "val3");

        rows.row(3).cell(col, ListCell.class).clickCollapseExpand();
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(5);
        rows.row(3).cell(col, ListCell.class).shouldHaveText(3, "val4");
        rows.row(3).cell(col, ListCell.class).shouldHaveText(4, "val5");
        rows.row(3).cell(col, ListCell.class).shouldHaveCollapseExpand(true);
        rows.row(3).cell(col, ListCell.class).clickCollapseExpand();
        rows.row(3).cell(col, ListCell.class).shouldHaveSize(3);
    }

}
