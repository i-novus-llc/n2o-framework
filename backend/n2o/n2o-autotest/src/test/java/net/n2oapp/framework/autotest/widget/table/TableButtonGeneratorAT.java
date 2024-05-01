package net.n2oapp.framework.autotest.widget.table;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.DropdownButton;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.button.N2oDropdownButton;
import net.n2oapp.framework.autotest.impl.component.button.N2oStandardButton;
import net.n2oapp.framework.autotest.impl.component.fieldset.N2oSimpleFieldSet;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.DownloadOptions.using;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TableButtonGeneratorAT extends AutoTestBase {

    private TableWidget table;
    private Toolbar toolbar;

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

        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/simple/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/simple/data.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/simple/exportModal.page.xml")
        );
    }

    @Test
    void testFilters() {
        openPage();

        table.columns().rows().shouldHaveSize(5);

        InputText inputName = table.filters().fields().field("Наименование").control(InputText.class);
        InputText inputRegion = table.filters().fields().field("Регион").control(InputText.class);
        Button search = table.filters().toolbar().button("Найти");
        Button reset = table.filters().toolbar().button("Сбросить");

        inputName.shouldBeVisible();
        inputRegion.shouldBeVisible();
        search.shouldBeVisible();
        reset.shouldBeVisible();

        toolbar.button(0, N2oStandardButton.class).shouldHaveIcon("fa fa-filter");
        toolbar.button(0, N2oStandardButton.class).click();
        inputName.shouldBeHidden();
        inputRegion.shouldBeHidden();
        search.shouldBeHidden();
        reset.shouldBeHidden();

        toolbar.button(0, N2oStandardButton.class).click();
        inputName.shouldBeVisible();
        inputRegion.shouldBeVisible();
        search.shouldBeVisible();
        reset.shouldBeVisible();

        inputName.setValue("ТМК");
        search.click();

        table.columns().rows().row(0).cell(2).shouldHaveText("ТМК");
        toolbar.button(0, N2oStandardButton.class).click();
        inputName.shouldBeHidden();
        inputRegion.shouldBeHidden();
        search.shouldBeHidden();
        reset.shouldBeHidden();
        table.columns().rows().shouldHaveSize(1);

        toolbar.button(0, N2oStandardButton.class).click();
        inputName.shouldHaveValue("ТМК");
        table.columns().rows().shouldHaveSize(1);
    }

    @Test
    void testColumns() {
        openPage();

        table.columns().headers().shouldHaveSize(4);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Наименование");
        table.columns().headers().header(3).shouldHaveTitle("Регион");
        table.paging().shouldExists();

        N2oDropdownButton button = toolbar.button(1, N2oDropdownButton.class);
        button.shouldBeCollapsed();
        button.shouldHaveIcon("fa fa-table");
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(4);
        shouldBeChecked(button.menuItem("Идентификатор"));
        shouldBeChecked(button.menuItem("Идентификатор ИПС"));
        shouldBeChecked(button.menuItem("Наименование"));
        shouldBeChecked(button.menuItem("Регион"));
        button.menuItem("Наименование").click();
        table.columns().headers().shouldHaveSize(3);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Регион");
        button.menuItem("Наименование").shouldNotHaveIcon();

        button.click();
        button.shouldBeCollapsed();
        table.columns().headers().shouldHaveSize(3);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Регион");
        table.paging().shouldExists();

        button.click();
        button.shouldBeExpanded();
        table.columns().headers().shouldHaveSize(3);
        shouldBeChecked(button.menuItem("Идентификатор"));
        shouldBeChecked(button.menuItem("Идентификатор ИПС"));
        shouldNotBeChecked(button.menuItem("Наименование"));
        shouldBeChecked(button.menuItem("Регион"));

        button.menuItem("Идентификатор").click();
        button.menuItem("Идентификатор").shouldNotHaveIcon();
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Регион");
        table.paging().shouldExists();

        button.menuItem("Идентификатор ИПС").click();
        button.menuItem("Регион").click();
        shouldNotBeChecked(button.menuItem("Идентификатор"));
        shouldNotBeChecked(button.menuItem("Идентификатор ИПС"));
        shouldNotBeChecked(button.menuItem("Наименование"));
        shouldNotBeChecked(button.menuItem("Регион"));
        table.columns().headers().shouldHaveSize(0);
        table.paging().shouldNotExists();

        button.menuItem("Наименование").click();
        shouldBeChecked(button.menuItem("Наименование"));
        table.columns().headers().shouldHaveSize(1);

        button.menuItem("Идентификатор ИПС").click();
        shouldBeChecked(button.menuItem("Идентификатор ИПС"));
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Наименование");

        button.menuItem("Регион").click();
        shouldBeChecked(button.menuItem("Регион"));
        table.columns().headers().shouldHaveSize(3);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Наименование");
        table.columns().headers().header(2).shouldHaveTitle("Регион");

        button.menuItem("Идентификатор").click();
        shouldBeChecked(button.menuItem("Идентификатор"));
        table.columns().headers().shouldHaveSize(4);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Наименование");
        table.columns().headers().header(3).shouldHaveTitle("Регион");
    }

    /**
     * Проверка заголовков колонок и настройки видимости колонок:
     * - при отсутствии icon и label
     * - только с icon
     * - только с label
     * - и с icon и с label
     */
    @Disabled
    @Test
    void testHeadersAndColumnsSettings() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator/check_headers");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/check_headers/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/check_headers/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/check_headers/exportModal.page.xml")
        );
        openPage();

        table.columns().headers().shouldHaveSize(4);
        table.columns().headers().header(0).shouldNotHaveTitle();
        table.columns().headers().header(0).shouldHaveIcon("fa-plus");
        table.columns().headers().header(1).shouldNotHaveTitle();
        table.columns().headers().header(1).shouldNotHaveIcon();
        table.columns().headers().header(2).shouldHaveTitle("Наименование");
        table.columns().headers().header(2).shouldHaveIcon("fa-pencil");
        table.columns().headers().header(3).shouldHaveTitle("Регион");
        table.columns().headers().header(1).shouldNotHaveIcon();

        N2oDropdownButton button = toolbar.button(0, N2oDropdownButton.class);
        button.shouldBeCollapsed();
        button.shouldHaveIcon("fa fa-table");
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(4);
        //клики необходимы, так как у кнопки две иконки в разных <i> элементах
        button.menuItem(0).click();
        button.menuItem(0).shouldHaveIcon("fa fa-plus");
        button.menuItem(1).shouldHaveDescription("id_ips");
        button.menuItem(2).click();
        button.menuItem(2).shouldHaveIcon("fa fa-pencil");
        button.menuItem(2).shouldHaveDescription("Наименование");
        button.menuItem(3).shouldHaveDescription("Регион");
    }

    @Disabled
    @Test
    void columnsVisibilityTest() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator/columns");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/columns/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/columns/data.query.xml")
        );

        openPage();

        InputText inpFolderType = table.filters().fields().field("folderType").control(InputText.class);
        DropdownButton button = toolbar.button(0, N2oDropdownButton.class);

        table.columns().headers().shouldHaveSize(1);
        table.columns().headers().header(0).shouldHaveTitle("ID");
        button.shouldBeCollapsed();
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(1);
        button.menuItem("ID").shouldExists();

        inpFolderType.setValue("1");
        inpFolderType.shouldHaveValue("1");
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("ID");
        table.columns().headers().header(1).shouldHaveTitle("Тип");
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(2);
        button.menuItem("ID").shouldExists();
        button.menuItem("Тип").shouldExists();

        button.menuItem("Тип").click();
        button.menuItem("Тип").shouldExists();
        table.columns().headers().shouldHaveSize(1);
        table.columns().headers().header(0).shouldHaveTitle("ID");

        button.menuItem("Тип").click();
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("ID");
        table.columns().headers().header(1).shouldHaveTitle("Тип");

        inpFolderType.clear();
        table.columns().headers().shouldHaveSize(1);
        table.columns().headers().header(0).shouldHaveTitle("ID");
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(1);
        button.menuItem("ID").shouldExists();
    }

    @Test
    void testRefresh() {
        openPage();

        Button create = table.toolbar().topLeft().button("Создать");
        Button delete = table.toolbar().topLeft().button("Удалить");
        Button refresh = table.toolbar().topRight().button(2, N2oStandardButton.class);

        table.columns().rows().row(0).cell(2).shouldHaveText("РМИС Республика Адыгея(СТП)");
        create.click();
        table.columns().rows().row(0).cell(2).shouldHaveText("РМИС Республика Адыгея(СТП)");
        refresh.click();
        table.columns().rows().row(0).cell(2).shouldHaveText("test");

        delete.click();
        table.columns().rows().row(0).cell(2).shouldHaveText("test");
        table.columns().rows().shouldHaveSize(5);
        refresh.click();
        table.columns().rows().shouldHaveSize(5);
        table.columns().rows().row(0).cell(2).shouldHaveText("РМИС Республика Адыгея(СТП)");
    }

    @Test
    void testResize() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator/resize");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/resize/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/resize/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/resize/data.object.xml")
        );

        openPage();

        N2oDropdownButton resize = table.toolbar().topRight().button(3, N2oDropdownButton.class);
        table.columns().rows().shouldHaveSize(5);
        table.paging().lastShouldHavePage("12");

        resize.click();
        resize.shouldHaveItems(4);
        resize.shouldBeExpanded();
        resize.menuItem("5").shouldHaveIcon("fa fa-check");
        resize.menuItem("10").shouldNotHaveIcon();
        resize.menuItem("20").shouldNotHaveIcon();
        resize.menuItem("50").shouldNotHaveIcon();

        resize.menuItem("10").click();
        resize.menuItem("5").shouldNotHaveIcon();
        resize.menuItem("10").shouldHaveIcon("fa fa-check");
        table.columns().rows().shouldHaveSize(10);
        table.paging().lastShouldHavePage("6");
        table.paging().selectPage("2");
        table.columns().rows().shouldHaveSize(10);
        table.paging().lastShouldHavePage("6");

        resize.shouldBeCollapsed();
        table.columns().rows().shouldHaveSize(10);

        resize.click();
        resize.menuItem("20").click();
        resize.menuItem("10").shouldNotHaveIcon();
        resize.menuItem("20").shouldHaveIcon("fa fa-check");
        table.columns().rows().shouldHaveSize(20);
        table.paging().lastShouldHavePage("3");

        resize.menuItem("50").click();
        resize.menuItem("20").shouldNotHaveIcon();
        resize.menuItem("50").shouldHaveIcon("fa fa-check");
        table.columns().rows().shouldHaveSize(50);
        table.paging().lastShouldHavePage("2");
        table.paging().selectPage("2");
        table.columns().rows().shouldHaveSize(10);
        table.paging().lastShouldHavePage("2");

        resize.shouldBeCollapsed();
        table.columns().rows().shouldHaveSize(10);
        table.paging().selectFirst();
        table.columns().rows().shouldHaveSize(50);
    }

    @Test
    void exportCurrentPageTest() throws IOException {
        openPage();

        table.paging().selectPage("2");

        StandardButton exportBtn = table.toolbar().topRight().button(5, StandardButton.class);
        exportBtn.shouldBeVisible();

        exportBtn.click();

        Modal modal = N2oSelenide.modal();
        StandardPage modalPage = modal.content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget form = modalPage.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);

        Select format = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Формат").control(Select.class);
        format.openPopup();
        format.dropdown().selectItemBy(Condition.text("CSV"));
        format.shouldHaveValue("CSV");

        Select charset = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Кодировка").control(Select.class);
        charset.openPopup();
        charset.dropdown().selectItemBy(Condition.text("UTF-8"));
        charset.shouldHaveValue("UTF-8");

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
            String expected = "\"Идентификатор\";\"Идентификатор ИПС\";\"Наименование\";\"Регион\"\n" +
                    "6;\"ey88ee-asd52a-89trd\";\"РМИС ХМАО-ЮГРЫ\";\"Ханты-Мансийский автономный округ - Югра\"\n" +
                    "7;\"oi44ew-asd52a-54eqw\";\"РС ЕГИСЗ Тюменской области\";\"Тюменская область\"\n" +
                    "8;\"ey88ee-mu67da-54eqw\";\"РМИАС Республика Башкортостан\";\"Республика Башкортостан\"\n" +
                    "9;\"ey88ee-56fhha-8hhjaf\";\"Промед Республики Хокасия\";\"Республика Хокасия\"\n" +
                    "10;\"asfa43-asd52a-asd4qd\";\"РИСЗ Республика Корелия\";\"Республика Корелия\"";
            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    void exportAllTableTest() throws IOException {
        openPage();
        StandardButton exportBtn = table.toolbar().topRight().button(5, StandardButton.class);
        exportBtn.shouldBeVisible();

        exportBtn.click();

        Modal modal = N2oSelenide.modal();
        StandardPage modalPage = modal.content(StandardPage.class);
        modalPage.shouldExists();

        FormWidget form = modalPage.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);

        Select format = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Формат").control(Select.class);
        format.openPopup();
        format.dropdown().selectItemBy(Condition.text("CSV"));
        format.shouldHaveValue("CSV");

        Select charset = form.fieldsets().fieldset(0, N2oSimpleFieldSet.class).fields().field("Кодировка").control(Select.class);
        charset.openPopup();
        charset.dropdown().selectItemBy(Condition.text("UTF-8"));
        charset.shouldHaveValue("UTF-8");

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
            String expected = "\"Идентификатор\";\"Идентификатор ИПС\";\"Наименование\";\"Регион\"\n" +
                    "1;\"ey88ee-rugh34-asd4\";\"РМИС Республика Адыгея(СТП)\";\"Республика Адыгея\"\n" +
                    "2;\"ey88ee-ruqah34-54eqw\";\"РМИС Республика Татарстан(тестовая для ПСИ)\";\"Республика Татарстан\"\n" +
                    "3;\"ey88ea-ruaah34-54eqw\";\"ТМК\";\"\"\n" +
                    "4;\"ey88ee-asd52a-54eqw\";\"МИС +МЕД\";\"Республика Адыгея\"\n" +
                    "5;\"ey88fe-asd52a-54eqb\";\"РМИС Комстромской области\";\"Комстромская область\"\n" +
                    "6;\"ey88ee-asd52a-89trd\";\"РМИС ХМАО-ЮГРЫ\";\"Ханты-Мансийский автономный округ - Югра\"\n" +
                    "7;\"oi44ew-asd52a-54eqw\";\"РС ЕГИСЗ Тюменской области\";\"Тюменская область\"\n" +
                    "8;\"ey88ee-mu67da-54eqw\";\"РМИАС Республика Башкортостан\";\"Республика Башкортостан\"\n" +
                    "9;\"ey88ee-56fhha-8hhjaf\";\"Промед Республики Хокасия\";\"Республика Хокасия\"\n" +
                    "10;\"asfa43-asd52a-asd4qd\";\"РИСЗ Республика Корелия\";\"Республика Корелия\"";
            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }

    private void openPage() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        table = page.widget(TableWidget.class);
        table.shouldExists();
        toolbar = table.toolbar().topRight();
    }

    /**
     * Проверка того, что колонка выбрана в настройках таблицы
     */
    private void shouldBeChecked(StandardButton button) {
        button.element().$(".n2o-dropdown-check-container .fa-check").should(Condition.exist);
    }

    /**
     * Проверка того, что колонка не выбрана в настройках таблицы
     */
    private void shouldNotBeChecked(StandardButton button) {
        button.element().$(".n2o-dropdown-check-container .fa-check").shouldNot(Condition.exist);
    }
}
