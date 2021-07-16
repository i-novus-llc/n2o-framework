package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.*;
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
public class ProgressBarCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/progress/index.page.xml"),
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
    public void progressBarCellTest() {
        int col = 0;

        rows.row(0).cell(col, ProgressBarCell.class).colorShouldBe(Colors.SUCCESS);
        rows.row(3).cell(col, ProgressBarCell.class).colorShouldBe(Colors.SUCCESS);

        rows.row(0).cell(col, ProgressBarCell.class).valueShouldBe("-50");
        rows.row(1).cell(col, ProgressBarCell.class).valueShouldBe("0");
        rows.row(2).cell(col, ProgressBarCell.class).valueShouldBe("100");
        rows.row(3).cell(col, ProgressBarCell.class).valueShouldBe("150");

        rows.row(0).cell(col, ProgressBarCell.class).sizeShouldBe(ProgressBarCell.Size.normal);
        rows.row(1).cell(col, ProgressBarCell.class).sizeShouldBe(ProgressBarCell.Size.normal);

        rows.row(1).cell(col, ProgressBarCell.class).shouldBeAnimated();
        rows.row(2).cell(col, ProgressBarCell.class).shouldBeAnimated();

        rows.row(2).cell(col, ProgressBarCell.class).shouldBeStriped();
        rows.row(3).cell(col, ProgressBarCell.class).shouldBeStriped();
    }

}
