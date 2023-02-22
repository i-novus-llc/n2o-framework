package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.LinkCell;
import net.n2oapp.framework.autotest.api.component.cell.RadioCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест способов выбора записей таблицы
 */
public class TableSelectionAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/test.query.xml"));
    }

    @Test
    public void testActiveSelection() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/active/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);
        rows.shouldBeSelected(0);
        rows.row(1).click();
        rows.shouldBeSelected(1);

        StandardButton button = table.toolbar().topLeft().button("Открыть");
        button.shouldBeEnabled();
        button.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Карточка клиента: 2");
        modal.close();
    }

    @Test
    public void testNoneSelection() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/none/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(2);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);
        rows.shouldNotHaveSelectedRows();
        rows.row(1).click();
        rows.shouldNotHaveSelectedRows();

        table.toolbar().topLeft().button("Открыть").shouldBeDisabled();
    }

    @Test
    public void testRadioSelection() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/radio/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(3);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);
        rows.shouldBeSelected(0);

        RadioCell cell1 = rows.row(0).cell(0, RadioCell.class);
        RadioCell cell2 = rows.row(1).cell(0, RadioCell.class);
        RadioCell cell3 = rows.row(2).cell(0, RadioCell.class);
        cell1.click();
        cell1.shouldBeChecked();
        cell2.shouldBeUnchecked();
        cell3.shouldBeUnchecked();
        cell2.click();
        cell2.shouldBeChecked();
        cell3.click();
        cell3.shouldBeChecked();
        cell1.shouldBeUnchecked();
        cell2.shouldBeUnchecked();

        rows.row(1).click();
        rows.shouldBeSelected(1);

        StandardButton button = table.toolbar().topLeft().button("Открыть");
        button.shouldBeEnabled();
        button.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Карточка клиента: 2");
        modal.close();
    }

    @Test
    public void testCheckboxSelection() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/checkbox/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().headers().shouldHaveSize(3);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);
        rows.shouldBeSelected(0);
        rows.row(1).click();
        rows.shouldBeSelected(1);

        CheckboxCell cell1 = rows.row(0).cell(0, CheckboxCell.class);
        CheckboxCell cell2 = rows.row(1).cell(0, CheckboxCell.class);
        CheckboxCell cell3 = rows.row(2).cell(0, CheckboxCell.class);
        cell1.shouldBeUnchecked();
        cell2.shouldBeChecked();
        cell3.shouldBeUnchecked();
        // выбор нескольких строк
        cell1.setChecked(true);
        cell1.shouldBeChecked();
        cell2.setChecked(false);
        cell2.shouldBeUnchecked();
        // выбор всех строк
        table.columns().headers().header(0).click();
        cell1.shouldBeChecked();
        cell2.shouldBeChecked();
        cell3.shouldBeChecked();
        // отмена выбора всех строк
        table.columns().headers().header(0).click();
        cell1.shouldBeUnchecked();
        cell2.shouldBeUnchecked();
        cell3.shouldBeUnchecked();

        StandardButton button = table.toolbar().topLeft().button("Открыть");
        button.shouldBeEnabled();
        button.click();

        Modal modal = N2oSelenide.modal();
        modal.shouldHaveTitle("Карточка клиента: 2");
        modal.close();
    }

    @Test
    void testSelectionNoneElementResolved() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/none/elements_resolve/index.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/none/elements_resolve/modal.page.xml"),
                        new CompileInfo("net/n2oapp/framework/autotest/widget/table/selection/none/elements_resolve/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        Modal modal = N2oSelenide.modal();
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(1).cell(2, ToolbarCell.class).toolbar().button(0, StandardButton.class).click();
        modal.shouldExists();
        modal.content(SimplePage.class).widget(FormWidget.class).fields().field("id").control(InputText.class).shouldHaveValue("2");
        modal.close();
        Selenide.refresh();
        table.columns().rows().row(1).cell(3, LinkCell.class).click();
        modal.shouldExists();
        modal.content(SimplePage.class).widget(FormWidget.class).fields().field("id").control(InputText.class).shouldHaveValue("2");
    }
}
