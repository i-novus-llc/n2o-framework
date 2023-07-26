package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.TextCell;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.Checkbox;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oSimpleFieldSet;
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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.DownloadOptions.using;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    public void testTable() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/simple");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/simple/testTable.query.xml"));
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
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldHaveIcon("fa-plus");
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }

        table.columns().headers().header(0).shouldBeVisible();
        table.columns().headers().header(0).shouldHaveTitle("Имя");
        table.columns().headers().header(0).shouldHaveStyle("color: red");
        table.columns().headers().header(1).shouldBeVisible();
        table.columns().headers().header(1).shouldHaveTitle("Фамилия");
        table.columns().headers().header(1).shouldHaveCssClass("font-italic");
        table.columns().headers().header(1).shouldHaveIcon("fa-plus");
        table.columns().headers().header(2).shouldBeVisible();
        table.columns().headers().header(2).shouldHaveTitle("Дата рождения");

        table.toolbar().topRight().button(1, DropdownButton.class).click();
        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();

        table.columns().headers().header(0).shouldBeVisible();
        table.columns().headers().header(1).shouldBeVisible();
        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeHidden();
            table.columns().rows().row(i).cell(2).shouldNotHaveIcon();
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }

        table.toolbar().topRight().button(1, DropdownButton.class).menuItem("Фамилия").click();

        table.columns().headers().header(0).shouldBeVisible();
        table.columns().headers().header(1).shouldBeVisible();
        table.columns().headers().header(1).shouldHaveIcon("fa-plus");
        table.columns().headers().header(2).shouldBeVisible();
        for (int i = 0; i < 3; i++) {
            table.columns().rows().row(i).cell(0).shouldBeHidden();
            table.columns().rows().row(i).cell(1).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldBeVisible();
            table.columns().rows().row(i).cell(2).shouldHaveIcon("fa-plus");
            table.columns().rows().row(i).cell(3).shouldBeVisible();
        }
    }

    @Test
    public void testRowClickEnabled() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/row_click");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/row_click/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);

        table.shouldExists();
        table.columns().rows().shouldHaveSize(4);

        Cells firstRow = table.columns().rows().row(0);
        firstRow.cell(1).shouldHaveText("1");
        firstRow.shouldNotBeClickable();
        Modal modal = N2oSelenide.modal();
        firstRow.click();
        modal.shouldNotExists();

        Cells thirdRow = table.columns().rows().row(2);
        thirdRow.cell(1).shouldHaveText("2");
        thirdRow.shouldBeClickable();
        thirdRow.click();
        modal.shouldExists();
        modal.close();
    }

    @Test
    public void testToolbar() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/toolbar/simple");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/simple/test.query.xml"));
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
    public void testHideOnBlur() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/toolbar/hide_on_blur");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/hide_on_blur/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/hide_on_blur/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/hide_on_blur/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(3);

        StandardButton button = rows.row(0).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
        rows.row(0).hover();
        button.shouldBeEnabled();
        button.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("echo");

        button = rows.row(1).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
        rows.row(1).hover();
        button.shouldExists();
        button.shouldBeDisabled();
        button = rows.row(2).cell(2, ToolbarCell.class).toolbar().button("Кнопка");
        button.shouldNotExists();
    }

    @Test
    public void testSortOfColumn() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/sort_column");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/sort_column/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/sort_column/test.query.xml"));

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
    public void testFetchOnClear() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/search_buttons");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/search_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/search_buttons/test.query.xml"));

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
    public void fetchOnVisibilityTest() {
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

    @Test
    public void exportCurrentPageTest() throws IOException {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/exportModal.page.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(3);
        table.paging().selectPage("2");

        StandardButton exportBtn = table.toolbar().topRight().button(Condition.cssClass("btn"));
        exportBtn.shouldBeVisible();

        exportBtn.click();

        Modal modal = N2oSelenide.modal();
        StandardPage modalPage = modal.content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget form = modalPage.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);

        InputText format = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Формат").control(InputText.class);
        format.shouldHaveValue("CSV");
        format.shouldBeDisabled();

        InputText charset = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Кодировка").control(InputText.class);
        charset.shouldHaveValue("UTF-8");
        charset.shouldBeDisabled();

        RadioGroup radioGroup = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Текущая страница").control(RadioGroup.class);
        radioGroup.check("Текущая страница");
        radioGroup.shouldBeChecked("Текущая страница");

        StandardButton download = modal.toolbar().bottomRight().button("Загрузить");
        download.shouldExists();
        StandardButton close = modal.toolbar().bottomRight().button("Закрыть");
        close.shouldExists();

        File file = download.element().download(
                using(FOLDER).withFilter(withExtension("csv"))
        );

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8)) {
            char[] chars = new char[(int) file.length() - 1];
            fileReader.read(chars);

            String actual = new String(chars);
            String expected = "id;id_;id_ips;name;region\n" +
                    "2;emdr_mris-2;ey88ee-ruqah34-54eqw;РМИС Республика Татарстан(тестовая для ПСИ);Республика Татарстан\n" +
                    "3;emdr_mris-3;ey88ea-ruaah34-54eqw;ТМК;\n" +
                    "4;emdr_mris-4;ey88ee-asd52a-54eqw;МИС +МЕД;Республика Адыгея\n" +
                    "\u0000";

            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void exportAllTableTest() throws IOException {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/toolbar/export_buttons/exportModal.page.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(3);

        StandardButton exportBtn = table.toolbar().topRight().button(Condition.cssClass("btn"));
        exportBtn.shouldBeVisible();

        exportBtn.click();

        Modal modal = N2oSelenide.modal();
        StandardPage modalPage = modal.content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget form = modalPage.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);

        InputText format = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Формат").control(InputText.class);
        format.shouldHaveValue("CSV");
        format.shouldBeDisabled();

        InputText charset = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Кодировка").control(InputText.class);
        charset.shouldHaveValue("UTF-8");
        charset.shouldBeDisabled();

        RadioGroup radioGroup = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Текущая страница").control(RadioGroup.class);
        radioGroup.shouldBeChecked("Загрузить все (но не более 1000 записей)");

        StandardButton download = modal.toolbar().bottomRight().button("Загрузить");
        download.shouldExists();
        StandardButton close = modal.toolbar().bottomRight().button("Закрыть");
        close.shouldExists();

        File file = download.element().download(
                using(FOLDER).withFilter(withExtension("csv"))
        );

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8)) {
            char[] chars = new char[(int) file.length() - 1];
            fileReader.read(chars);

            String actual = new String(chars);
            String expected = "id;id_;id_ips;name;region\n" +
                    "13;eadad;asdaa;asdads;adad\n" +
                    "12;asd;asd;adad;asdada\n" +
                    "1;emdr_mris-1;ey88ee-rugh34-asd4;РМИС Республика Адыгея(СТП);Республика Адыгея\n" +
                    "2;emdr_mris-2;ey88ee-ruqah34-54eqw;РМИС Республика Татарстан(тестовая для ПСИ);Республика Татарстан\n" +
                    "3;emdr_mris-3;ey88ea-ruaah34-54eqw;ТМК;\n" +
                    "4;emdr_mris-4;ey88ee-asd52a-54eqw;МИС +МЕД;Республика Адыгея\n" +
                    "\u0000";

            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }

    private void verifyNeverGetDataInvocation(int times, String errorMessage) {
        try {
            verify(controller, times(times)).getData(any());
        } catch (TooManyActualInvocations e) {
            throw new AssertionError(errorMessage);
        }
    }
}
