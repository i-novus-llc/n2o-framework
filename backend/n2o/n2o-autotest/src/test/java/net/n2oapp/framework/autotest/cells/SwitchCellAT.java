package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.cell.BadgeCell;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.IconCell;
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
 * Автотест для переключателя ячеек
 */
public class SwitchCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/cells/switch/index.page.xml"),
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
    public void switchCellTest() {
        BadgeCell cell1 = rows.row(0).cell(0, BadgeCell.class);
        cell1.shouldExists();
        cell1.textShouldHave("test1");
        cell1.colorShouldBe(Colors.INFO);
        IconCell cell2 = rows.row(1).cell(0, IconCell.class);
        cell2.shouldExists();
        cell2.textShouldHave("test2");
        cell2.iconShouldBe("fa-plus");
        CheckboxCell cell3 = rows.row(2).cell(0, CheckboxCell.class);
        cell3.shouldExists();
        cell3.shouldBeChecked();
        TextCell cell4 = rows.row(3).cell(0, TextCell.class);
        cell4.shouldExists();
        cell4.textShouldHave("test4");
    }
}
