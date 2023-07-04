package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
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
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.DownloadOptions.using;
import static com.codeborne.selenide.FileDownloadMode.FOLDER;
import static com.codeborne.selenide.files.FileFilters.withExtension;
import static org.junit.jupiter.api.Assertions.*;

public class TableButtonGeneratorAT extends AutoTestBase {

    private SimplePage page;

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
        page = open(SimplePage.class);
        page.shouldExists();
        table = page.widget(TableWidget.class);
        toolbar = table.toolbar().topRight();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/data.object.xml"));
    }

    @Test
    public void testFilters() {
        InputText inputName = table.filters().fields().field("Наименование").control(InputText.class);
        InputText inputRegion = table.filters().fields().field("Регион").control(InputText.class);
        Button search = table.filters().toolbar().button("Найти");
        Button reset = table.filters().toolbar().button("Сбросить");

        inputName.shouldBeVisible();
        inputRegion.shouldBeVisible();
        search.shouldBeVisible();
        reset.shouldBeVisible();

        toolbar.button(0, N2oStandardButton.class).shouldHaveIcon("fa-filter");
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
//        inputName.shouldHaveValue("ТМК");
        table.columns().rows().shouldHaveSize(1);
    }

    @Test
    public void testColumns() {
        table.columns().headers().shouldHaveSize(4);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Наименование");
        table.columns().headers().header(3).shouldHaveTitle("Регион");

        N2oDropdownButton button = toolbar.button(1, N2oDropdownButton.class);
        button.shouldBeCollapsed();
        button.shouldHaveIcon("fa-table");
        button.click();
        button.shouldBeExpanded();
        button.shouldHaveItems(4);
        button.menuItem("Идентификатор").shouldHaveIcon("fa-check");
        button.menuItem("Идентификатор ИПС").shouldHaveIcon("fa-check");
        button.menuItem("Наименование").shouldHaveIcon("fa-check");
        button.menuItem("Регион").shouldHaveIcon("fa-check");
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

        button.click();
        button.shouldBeExpanded();
        table.columns().headers().shouldHaveSize(3);
        button.menuItem("Идентификатор").shouldHaveIcon("fa-check");
        button.menuItem("Идентификатор ИПС").shouldHaveIcon("fa-check");
        button.menuItem("Наименование").shouldNotHaveIcon();
        button.menuItem("Регион").shouldHaveIcon("fa-check");

        button.menuItem("Идентификатор").click();
        button.menuItem("Идентификатор").shouldNotHaveIcon();
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Регион");

        button.menuItem("Идентификатор ИПС").click();
        button.menuItem("Регион").click();
        button.menuItem("Идентификатор").shouldNotHaveIcon();
        button.menuItem("Идентификатор ИПС").shouldNotHaveIcon();
        button.menuItem("Наименование").shouldNotHaveIcon();
        button.menuItem("Регион").shouldNotHaveIcon();
        table.columns().headers().shouldHaveSize(0);

        button.menuItem("Наименование").click();
        button.menuItem("Наименование").shouldHaveIcon("fa-check");
        table.columns().headers().shouldHaveSize(1);

        button.menuItem("Идентификатор ИПС").click();
        button.menuItem("Идентификатор ИПС").shouldHaveIcon("fa-check");
        table.columns().headers().shouldHaveSize(2);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Наименование");

        button.menuItem("Регион").click();
        button.menuItem("Регион").shouldHaveIcon("fa-check");
        table.columns().headers().shouldHaveSize(3);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(1).shouldHaveTitle("Наименование");
        table.columns().headers().header(2).shouldHaveTitle("Регион");

        button.menuItem("Идентификатор").click();
        button.menuItem("Идентификатор").shouldHaveIcon("fa-check");
        table.columns().headers().shouldHaveSize(4);
        table.columns().headers().header(0).shouldHaveTitle("Идентификатор");
        table.columns().headers().header(1).shouldHaveTitle("Идентификатор ИПС");
        table.columns().headers().header(2).shouldHaveTitle("Наименование");
        table.columns().headers().header(3).shouldHaveTitle("Регион");
    }

    @Test
    public void testRefresh() {
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
    public void testResize() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/button_generator/resize");
        N2oDropdownButton resize = table.toolbar().topRight().button(3, N2oDropdownButton.class);
        table.columns().rows().shouldHaveSize(5);
        table.paging().lastShouldHavePage("12");

        resize.click();
        resize.shouldHaveItems(4);
        resize.shouldBeExpanded();
        resize.menuItem("5").shouldHaveIcon("fa-check");
        resize.menuItem("10").shouldNotHaveIcon();
        resize.menuItem("20").shouldNotHaveIcon();
        resize.menuItem("50").shouldNotHaveIcon();

        resize.menuItem("10").click();
        resize.menuItem("5").shouldNotHaveIcon();
        resize.menuItem("10").shouldHaveIcon("fa-check");
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
        resize.menuItem("20").shouldHaveIcon("fa-check");
        table.columns().rows().shouldHaveSize(20);
        table.paging().lastShouldHavePage("3");

        resize.menuItem("50").click();
        resize.menuItem("20").shouldNotHaveIcon();
        resize.menuItem("50").shouldHaveIcon("fa-check");
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
    public void exportCurrentPageTest() throws IOException {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/exportModal.page.xml"));
        table.shouldExists();
        table.paging().selectPage("2");

        StandardButton exportBtn = table.toolbar().topRight().button(5, StandardButton.class);
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
            String expected = "id;id_ips;name;region\n" +
                    "6;ey88ee-asd52a-89trd;РМИС ХМАО-ЮГРЫ;Ханты-Мансийский автономный округ - Югра\n" +
                    "7;oi44ew-asd52a-54eqw;РС ЕГИСЗ Тюменской области;Тюменская область\n" +
                    "8;ey88ee-mu67da-54eqw;РМИАС Республика Башкортостан;Республика Башкортостан\n" +
                    "9;ey88ee-56fhha-8hhjaf;Промед Республики Хокасия;Республика Хокасия";
            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void exportAllTableTest() throws IOException {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/table/button_generator/exportModal.page.xml"));
        StandardButton exportBtn = table.toolbar().topRight().button(5, StandardButton.class);
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
            String expected = "id;id_ips;name;region\n" +
                    "1;ey88ee-rugh34-asd4;РМИС Республика Адыгея(СТП);Республика Адыгея\n" +
                    "2;ey88ee-ruqah34-54eqw;РМИС Республика Татарстан(тестовая для ПСИ);Республика Татарстан\n" +
                    "3;ey88ea-ruaah34-54eqw;ТМК;\n" +
                    "4;ey88ee-asd52a-54eqw;МИС +МЕД;Республика Адыгея";
            assertTrue(actual.contains(expected), "Экспортированное значение таблицы не соответствует ожидаемому");
        } catch (IOException e) {
            fail();
        }
    }
}