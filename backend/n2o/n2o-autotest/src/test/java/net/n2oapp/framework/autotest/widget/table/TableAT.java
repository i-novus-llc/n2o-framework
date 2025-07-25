package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.cell.IconCell;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.N2oController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Автотест для виджета Таблица
 */
class TableAT extends AutoTestBase {

    @SpyBean
    private N2oController controller;

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testTable() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/simple/testTable.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.filters().shouldBeVisible();
        table.filters().toolbar().button("searchLabel").shouldBeEnabled();
        table.filters().toolbar().button("resetLabel").shouldBeDisabled();
        table.filters().fields().field("Имя").control(InputText.class).click();
        table.filters().fields().field("Имя").control(InputText.class).setValue("test");
        Select select = table.filters().fields().field("Пол").control(Select.class);
        select.openPopup();
        select.dropdown().selectItemBy(Condition.text("Мужской"));
        table.filters().toolbar().button("resetLabel").click();
        table.filters().fields().field("Имя").control(InputText.class).shouldHaveValue("test");

        table.toolbar().topRight().button(0, StandardButton.class).click();
        table.filters().shouldBeHidden();
        table.toolbar().topRight().button(0, StandardButton.class).click();

        ColorsEnum[] colors = new ColorsEnum[]{ColorsEnum.DANGER, ColorsEnum.INFO, ColorsEnum.SUCCESS};

        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).shouldHaveColor(colors[i]);
            table.columns().rows().row(i).cell(0).shouldBeVisible();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldHaveIcon("fa-plus");
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }
        table.columns().headers().header(0).shouldHaveTitle("id");
        table.filters().fields().field("Имя").control(InputText.class).clear();
        table.filters().toolbar().button("searchLabel").click();

        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(0).shouldHaveStyle("color: red");
        table.columns().headers().header(1).shouldHaveTitle("Фамилия");
        table.columns().headers().header(1).shouldHaveCssClass("font-italic");
        table.columns().headers().header(1).shouldHaveIcon("fa-plus");
        table.columns().headers().header(2).shouldHaveTitle("Дата рождения");

        table.toolbar().topRight().button(1, DropdownButton.class).click();
        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();
        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(1).shouldHaveTitle("Дата рождения");

        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();
        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(1).shouldHaveTitle("Фамилия");
        table.columns().headers().header(2).shouldHaveTitle("Дата рождения");
    }

    @Test
    void testOverlay() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/row_overlay");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_overlay/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_overlay/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();

        table.shouldExists();
        rows.shouldHaveSize(4);

        rows.row(0).hover();
        Toolbar toolbar = page.overlay().toolbar();
        toolbar.shouldHaveSize(3);

        StandardButton button = toolbar.button("Кнопка");
        button.shouldExists();
        button.click();
        page.alerts(Alert.PlacementEnum.TOP_LEFT).alert(0).shouldHaveTitle("Уведомление по кнопке");

        DropdownButton dropdownButton = toolbar.dropdown();
        dropdownButton.shouldExists();
        dropdownButton.shouldHaveItems(1);
        dropdownButton.click();

        dropdownButton.menuItem("Кнопка1").shouldExists();
        dropdownButton.menuItem("Кнопка1").click();
        page.alerts(Alert.PlacementEnum.TOP_RIGHT).alert(0).shouldHaveTitle("Уведомление по menu-item");

    }

    @Test
    void testToolbar() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/toolbar/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/simple/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);

        StandardButton button = rows.row(0).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldExists();
        button.shouldBeEnabled();
        button = rows.row(1).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldExists();
        button.shouldBeDisabled();
        button = rows.row(2).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
    }

    @Test
    void testSortOfColumn() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/sort_column");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/sort_column/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/sort_column/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        TextCell cellFirst = rows.row(0).cell(1);
        TextCell cellSecond = rows.row(1).cell(1);
        TextCell cellThird = rows.row(2).cell(1);
        TextCell cellFourth = rows.row(3).cell(1);

        TableHeader secondHeader = table.columns().headers().header(1, TableHeader.class);
        secondHeader.shouldBeSortedByDesc();

        cellFirst.shouldBeVisible();
        cellSecond.shouldBeVisible();
        cellThird.shouldBeVisible();
        cellFourth.shouldBeVisible();

        cellFirst.shouldHaveText("test4");
        cellSecond.shouldHaveText("test3");
        cellThird.shouldHaveText("test2");
        cellFourth.shouldHaveText("test1");

        secondHeader.click();
        secondHeader.click();
        secondHeader.shouldBeSortedByAsc();

        cellFirst.shouldHaveText("test1");
        cellSecond.shouldHaveText("test2");
        cellThird.shouldHaveText("test3");
        cellFourth.shouldHaveText("test4");

        TableHeader firstHeader = table.columns().headers().header(0, TableHeader.class);
        firstHeader.click();
        firstHeader.shouldBeSortedByAsc();
        page.shouldHaveUrlMatches(".*sorting_w1_id=ASC");
        firstHeader.click();
        firstHeader.shouldBeSortedByDesc();
        page.shouldHaveUrlMatches(".*sorting_w1_id=DESC");

        cellFirst.shouldHaveText("test4");
        cellSecond.shouldHaveText("test3");
        cellThird.shouldHaveText("test2");
        cellFourth.shouldHaveText("test1");

        Selenide.refresh();
        page.shouldHaveUrlMatches(".*sorting_w1_id=DESC");
        firstHeader.shouldBeSortedByDesc();
    }

    @Test
    void fetchOnVisibilityTest() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/fetch_on_visibility");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_visibility/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_visibility/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems regionItems = page.regions().region(0, SimpleRegion.class).content();
        FormWidget form = regionItems.widget(0, FormWidget.class);
        TableWidget table = regionItems.widget(1, TableWidget.class);

        form.shouldBeVisible();
        table.shouldBeVisible();

        Checkbox checkbox = form.fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("Видимость таблицы")
                .control(Checkbox.class);
        checkbox.shouldBeChecked();

        checkbox.setChecked(false);
        table.shouldBeHidden();

        checkbox.setChecked(true);
        table.shouldBeVisible();

        verifyNeverGetDataInvocation(1, "Запрос за данными таблицы при fetch-on-visibility=false");
    }

    private void verifyNeverGetDataInvocation(int times, String errorMessage) {
        try {
            verify(controller, times(times)).getData(any());
        } catch (TooManyActualInvocations e) {
            throw new AssertionError(errorMessage);
        }
    }

    @Test
    void testAlignment() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/alignment");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/alignment/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/alignment/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.shouldExists();

        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(4);
        headers.header(0, TableHeader.class).shouldHaveAlignment("center");
        headers.header(1, TableHeader.class).shouldHaveAlignment("right");
        headers.header(2, TableHeader.class).shouldHaveAlignment("right");
        headers.header(3, TableHeader.class).shouldHaveAlignment("left");

        TableWidget.Rows rows = table.columns().rows();
        rows.row(0).cell(0).shouldHaveAlignment("center");
        rows.row(0).cell(1).shouldHaveAlignment("right");
        rows.row(0).cell(2).shouldHaveAlignment("left");
        rows.row(0).cell(3).shouldHaveAlignment("right");
    }

    @Test
    void testCellTooltip() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/cell_tooltip");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/cell_tooltip/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/cell_tooltip/test.query.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget.Rows rows = page.widget(TableWidget.class).columns().rows();
        rows.shouldHaveSize(4);
        int cellIndex;

        // Ячейка text
        cellIndex = 0;
        TextCell textCell = rows.row(2).cell(cellIndex, TextCell.class);
        textCell.hover();
        Tooltip tooltip = textCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_text3"});

        textCell = rows.row(1).cell(cellIndex, TextCell.class);
        textCell.hover();
        tooltip = textCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_text2"});

        textCell = rows.row(0).cell(cellIndex, TextCell.class);
        textCell.hover();
        tooltip = textCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_text1"});


        // Ячейка checkbox
        cellIndex = 1;
        CheckboxCell checkboxCell = rows.row(2).cell(cellIndex, CheckboxCell.class);
        checkboxCell.hover();
        tooltip = checkboxCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_checkbox3"});

        checkboxCell = rows.row(1).cell(cellIndex, CheckboxCell.class);
        checkboxCell.hover();
        tooltip = checkboxCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_checkbox2"});

        checkboxCell = rows.row(0).cell(cellIndex, CheckboxCell.class);
        checkboxCell.hover();
        tooltip = checkboxCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_checkbox1"});


        // Ячейка icon
        cellIndex = 2;
        // идем снизу вверх, чтобы tooltip не перекрывал ячейку
        IconCell iconCell = rows.row(2).cell(cellIndex, IconCell.class);
        iconCell.hover();
        tooltip = iconCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_icon3"});

        iconCell = rows.row(1).cell(cellIndex, IconCell.class);
        iconCell.hover();
        tooltip = iconCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_icon2"});

        iconCell = rows.row(0).cell(cellIndex, IconCell.class);
        iconCell.hover();
        tooltip = iconCell.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText(new String[]{"tooltip_icon1"});
    }

    @Test
    void testClearFilters() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/clear_filters");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/clear_filters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/clear_filters/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        StandardButton button = table.filters().toolbar().button("Сбросить");
        InputText input = table.filters().fields().field("name").control(InputText.class);

        button.shouldBeDisabled();
        input.setValue("test");
        button.shouldBeEnabled();
        input.clear();
        button.shouldBeDisabled();

        InputSelect select = table.filters().fields().field("type").control(InputSelect.class);
        select.openPopup();
        button.shouldBeDisabled();
        select.dropdown().selectItem(1);
        button.shouldBeEnabled();
        select.clear();
        button.shouldBeDisabled();
    }
}
