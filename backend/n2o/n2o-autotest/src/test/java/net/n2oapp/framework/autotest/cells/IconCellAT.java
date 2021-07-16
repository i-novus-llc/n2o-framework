package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.*;
import net.n2oapp.framework.autotest.api.component.page.Page;
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
public class IconCellAT extends AutoTestBase {

    private TableWidget.Rows rows;
    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/icon/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/testTable.query.xml"));

        page = open(SimplePage.class);
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
    public void iconCellTest() {
        int col = 0;

        rows.row(0).cell(col, IconCell.class).iconShouldBe("fa-phone");
        rows.row(0).cell(col, IconCell.class).textShouldHave("fa fa-phone");
        rows.row(3).cell(col, IconCell.class).iconShouldBe("fa-minus");
        rows.row(3).cell(col, IconCell.class).textShouldHave("fa fa-minus");
    }

    @Test
    public void iconCellTooltipTest() {
        Page.Tooltip tooltip = page.tooltip();

        // идем снизу вверх, чтобы tooltip не перекрывал ячейку
        rows.row(3).cell(0, IconCell.class).hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("minus");
        rows.row(2).cell(0, IconCell.class).hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("fax");
        rows.row(1).cell(0, IconCell.class).hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("plus");
        rows.row(0).cell(0, IconCell.class).hover();
        tooltip.shouldBeExist();
        tooltip.shouldHaveText("phone");
    }
}
