package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
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
public class TableAT extends AutoTestBase {

    @SpyBean
    private N2oController controller;

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void testTable() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/simple");
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

        table.columns().rows().row(0).shouldHaveColor(Colors.DANGER);
        table.columns().rows().row(1).shouldHaveColor(Colors.INFO);
        table.columns().rows().row(2).shouldHaveColor(Colors.SUCCESS);

        for (int i = 0; i < 3; i++) {
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
    void testRowClickEnabled() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/row_click");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);

        Cells firstRow = table.columns().rows().row(0);
        firstRow.cell(1).shouldHaveText("1");
        Modal modal = N2oSelenide.modal();
        firstRow.click();
        modal.shouldNotExists();

        Cells thirdRow = table.columns().rows().row(2);
        thirdRow.cell(1).shouldHaveText("2");
        thirdRow.click();
        modal.shouldExists();
        modal.close();
    }

    @Test
    void testOverlay() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/row_overlay");
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
        page.alerts(Alert.Placement.topLeft).alert(0).shouldHaveTitle("Уведомление по кнопке");

        DropdownButton dropdownButton = toolbar.dropdown();
        dropdownButton.shouldExists();
        dropdownButton.shouldHaveItems(1);
        dropdownButton.click();

        dropdownButton.menuItem("Кнопка1").shouldExists();
        dropdownButton.menuItem("Кнопка1").click();
        page.alerts(Alert.Placement.topRight).alert(0).shouldHaveTitle("Уведомление по menu-item");

    }

    @Test
    void testToolbar() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/toolbar/simple");
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
        setJsonPath("net/n2oapp/framework/autotest/widget/table/sort_column");
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
    }

    @Test
    void testFetchOnClear() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/search_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/search_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/search_buttons/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText input = table.filters().fields().field("name").control(InputText.class);
        input.click();
        input.setValue("test");
        table.filters().toolbar().button("Найти").click();
        table.columns().rows().shouldHaveSize(4);

        table.filters().toolbar().button("Сбросить").click();
        table.columns().rows().shouldHaveSize(0);
        verifyNeverGetDataInvocation(2, "Запрос за данными таблицы при fetch-on-clear=false");
        input.shouldBeEmpty();
        table.paging().shouldNotHaveTotalElements();
    }

    @Test
    void fetchOnVisibilityTest() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/fetch_on_visibility");
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
}
