package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.IconCell;
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
class IconCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        setResourcePath("net/n2oapp/framework/autotest/cells/icon");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/icon/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/icon/testTable.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oCellsPack(), new N2oAllDataPack());
    }


    @Test
    void iconCellTest() {
        int col = 0;
        rows.row(0).cell(col, IconCell.class).shouldHaveIcon("fa-phone");
        rows.row(0).cell(col, IconCell.class).shouldHaveText("fa fa-phone");
        rows.row(3).cell(col, IconCell.class).shouldHaveIcon("fa-minus");
        rows.row(3).cell(col, IconCell.class).shouldHaveText("fa fa-minus");
    }
}