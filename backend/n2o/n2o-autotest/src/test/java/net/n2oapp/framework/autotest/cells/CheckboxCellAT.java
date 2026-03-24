package net.n2oapp.framework.autotest.cells;

import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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
 * Автотест ячеек таблицы
 */
class CheckboxCellAT extends AutoTestBase {

    private TableWidget.Rows rows;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
    }

    @Test
    void test() {
        setResourcePath("net/n2oapp/framework/autotest/cells/checkbox/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/checkbox/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/checkbox/simple/data.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
        int col = 0;

        rows.row(0).cell(col, CheckboxCell.class).shouldBeChecked();
        rows.row(1).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(2).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(2).cell(col, CheckboxCell.class).shouldBeDisabled();
        rows.row(0).cell(col, CheckboxCell.class).setChecked(false);
        rows.row(1).cell(col, CheckboxCell.class).setChecked(true);
        rows.row(0).cell(col, CheckboxCell.class).shouldBeUnchecked();
        rows.row(1).cell(col, CheckboxCell.class).shouldBeChecked();
    }

    @Test
    void testFormParam() {
        setResourcePath("net/n2oapp/framework/autotest/cells/checkbox/form_param");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/cells/checkbox/form_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/checkbox/form_param/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/cells/checkbox/form_param/data.object.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);

        rows.row(0).cell(1, CheckboxCell.class).shouldBeChecked();
        rows.row(1).cell(1, CheckboxCell.class).shouldBeUnchecked();
        rows.row(2).cell(1, CheckboxCell.class).shouldBeChecked();
        rows.row(3).cell(1, CheckboxCell.class).shouldBeUnchecked();
        rows.shouldBeSelected(0);

        rows.row(1).cell(1, CheckboxCell.class).setChecked(true);
        rows.shouldBeSelected(1);
        Alert alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("2");

        rows.row(2).cell(1, CheckboxCell.class).setChecked(false);
        rows.shouldBeSelected(2);
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("3");

        rows.row(1).cell(0).click();
        rows.shouldBeSelected(1);
        rows.row(1).cell(1, CheckboxCell.class).setChecked(false);
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("2");

        rows.row(2).cell(0).click();
        rows.shouldBeSelected(2);
        rows.row(2).cell(1, CheckboxCell.class).setChecked(true);
        alert = page.alerts(Alert.PlacementEnum.TOP).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("3");
    }
}
