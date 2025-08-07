package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.ColorsEnum;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.autotest.N2oSelenide.page;

/**
 * Автотест компонента ввода текста с выбором из выпадающего списка
 */
class InputSelectAT extends AutoTestBase {

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
                new N2oRegionsPack(),
                new N2oPagesPack(),
                new N2oApplicationPack(),
                new N2oWidgetsPack(),
                new N2oActionsPack(),
                new N2oFieldSetsPack(),
                new N2oControlsPack(),
                new N2oAllDataPack(),
                new N2oCellsPack()
        );
    }

    @Test
    void testSingle() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/simple/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        InputSelect input = fields.field("InputSelect").control(InputSelect.class);
        input.shouldExists();

        // close popup by icon
        input.openPopup();
        input.shouldBeOpened();
        input.closePopup();
        input.shouldBeClosed();

        input.shouldBeEmpty();
        input.openPopup();
        input.dropdown().shouldHaveOptions(new String[]{"One", "Two", "Three"});
        // close popup by click on options
        input.openPopup();
        input.dropdown().selectItem(1);
        input.shouldBeClosed();
        input.shouldHaveValue("Two");

        // Выбранная строка в выпадающем списке не должна быть кликабельной
        input.openPopup();
        input.dropdown().item(1).shouldBeSelected();
        input.dropdown().item(1).shouldBeDisabled();

        input.clear();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().item(1).shouldNotBeSelected();

        input.setValue("Three");
        input.pressEnter();
        input.shouldBeClosed();
        input.shouldHaveValue("Three");

        // Выбранная строка в выпадающем списке не должна быть кликабельной
        input.openPopup();
        input.dropdown().item(2).shouldBeSelected();
        input.dropdown().item(2).shouldBeDisabled();

        InputSelect input2 = fields.field("InputSelect1").control(InputSelect.class);
        input2.click();
        input2.dropdown().item("One").shouldBeEnabled();
        input2.dropdown().item("Two").shouldBeEnabled();
        input2.dropdown().item("Three").shouldBeDisabled();
        input2.dropdown().item("Four").shouldBeEnabled();

        // close popup by click on outside area
        input2.openPopup();
        input2.shouldBeOpened();
        input.click();
        input2.shouldBeClosed();
    }

    @Test
    void testColorStatus() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/simple/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect2")
                .control(InputSelect.class);
        input.shouldExists();

        input.click();
        DropDown dropdown = input.dropdown();
        dropdown.item("One").shouldHaveStatusColor(ColorsEnum.SUCCESS);
        dropdown.item("Two").shouldHaveStatusColor(ColorsEnum.PRIMARY);
        dropdown.item("Three").shouldHaveStatusColor(ColorsEnum.DANGER);
    }

    @Test
    void testMulti() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/multi");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/multi/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/multi/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        input.dropdown().shouldHaveOptions(new String[]{"One", "Two", "Three"});
        input.openPopup();
        input.dropdown().selectMulti(1, 2);
        input.shouldSelectedMulti(new String[]{"Two", "Three"});
        input.clearUsingIcon();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().selectMulti(2, 1, 0);
        input.shouldSelectedMulti(new String[]{"Three", "Two", "One"});

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti(new String[]{"Two", "One"});
        input.clearItems("Two", "One");
        input.shouldBeEmpty();

        // проверяем удаление элементов через Backspace
        input.openPopup();
        input.dropdown().selectMulti(2, 1, 0);
        input.shouldSelectedMulti(new String[]{"Three", "Two", "One"});
        input.backspace();
        input.shouldSelectedMulti(new String[]{"Three", "Two"});
        input.backspace();
        input.shouldSelectedMulti(new String[]{"Three"});
        input.backspace();
        input.shouldBeEmpty();

        // Проверка сохранения фильтра при выборе нескольких значений
        input = page.widget(FormWidget.class).fields().field("InputSelect1").control(InputSelect.class);
        input.openPopup();
        input.dropdown().shouldHaveOptions(new String[]{"1","2","11","100","101"});

        input.setValue("1");
        input.dropdown().shouldHaveOptions(new String[]{"1","11","100","101"});

        input.dropdown().selectMulti(0);
        input.dropdown().shouldHaveOptions(new String[]{"1","11","100","101"});
        input.shouldSelectedMulti(new String[]{"1"});

        input.dropdown().selectMulti(2);
        input.dropdown().shouldHaveOptions(new String[]{"1","11","100","101"});
        input.shouldSelectedMulti(new String[]{"1","100"});
    }

    @Test
    void testCheckboxes() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/checkboxes/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        input.dropdown().shouldHaveOptions(new String[]{"One", "Two", "Three"});
        input.openPopup();
        input.dropdown().selectMulti(1, 2);
        input.shouldSelectedMulti(new String[]{"Two", "Three"});
        input.clearUsingIcon();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().selectMulti(2, 1, 0);
        input.shouldSelectedMulti(new String[]{"Three", "Two", "One"});

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti(new String[]{"Two", "One"});
        input.clearItems("Two", "One");
        input.shouldBeEmpty();

        input.closePopup();
        input = page.widget(FormWidget.class).fields().field("InputSelect3")
                .control(InputSelect.class);
        input.click();
        input.dropdown().item("One").shouldBeEnabled();
        input.dropdown().item("Two").shouldBeEnabled();
        input.dropdown().item("Three").shouldBeDisabled();
        input.dropdown().item("Four").shouldBeEnabled();
    }

    @Test
    void testReadFromQuery() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/query");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/query/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/query/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect1")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        DropDown dropdown = input.dropdown();
        dropdown.shouldHaveOptions(new String[]{"name1", "name2", "name3"});
        input.openPopup();
        dropdown.item("name1").shouldHaveDescription("desc1");
        input.openPopup();
        dropdown.item("name3").shouldHaveDescription("desc3");
        input.openPopup();
        dropdown.selectItem(1);
        input.shouldHaveValue("name2");
        input.clear();
        input.shouldBeEmpty();
        input.setValue("name3");
        input.shouldHaveValue("name3");
        // сворачиваем popup, чтобы не накладывался на нижний контрол
        input.closePopup();


        InputSelect input2 = page.widget(FormWidget.class).fields().field("InputSelect2")
                .control(InputSelect.class);
        input2.shouldExists();

        input2.shouldBeEmpty();
        input2.openPopup();
        DropDown dropdown2 = input2.dropdown();
        dropdown2.item("name1").shouldHaveDescription("desc1");
        input2.openPopup();
        dropdown2.item("name3").shouldHaveDescription("desc3");
        input2.openPopup();
        dropdown2.shouldHaveOptions(new String[]{"name1", "name2", "name3"});
        input2.openPopup();
        dropdown2.selectMulti(1, 2);
        input2.shouldSelectedMulti(new String[]{"name2", "name3"});
        input2.clearUsingIcon();
        input2.shouldBeEmpty();
    }

    @Test
    void testPrefilterByQueryParam() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/gender.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.shouldExists();
        StandardButton open = tableWidget.toolbar().topLeft().button("Open");
        open.shouldExists();
        open.click();

        SimplePage newPage = page(SimplePage.class);
        newPage.shouldExists();
        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();
        InputSelect gender = formWidget.fields().field("Gender").control(InputSelect.class);
        InputSelect genderWithConst = formWidget.fields().field("Gender with const").control(InputSelect.class);
        gender.shouldExists();
        gender.shouldHaveValue("Мужской");
        gender.openPopup();
        gender.dropdown().shouldHaveOptions(new String[]{"Мужской"});
        genderWithConst.shouldExists();
        genderWithConst.shouldHaveValue("Женский");
        genderWithConst.openPopup();
        genderWithConst.dropdown().shouldHaveOptions(new String[]{"Женский"});
    }

    @Test
    void testPrefilterByPathParam() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/gender.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget tableWidget = page.widget(TableWidget.class);
        tableWidget.shouldExists();
        StandardButton open = tableWidget.toolbar().topLeft().button("Open");
        open.shouldExists();
        open.click();

        SimplePage newPage = page(SimplePage.class);
        newPage.shouldExists();
        FormWidget formWidget = page.widget(FormWidget.class);
        formWidget.shouldExists();
        InputSelect gender = formWidget.fields().field("Gender").control(InputSelect.class);
        InputSelect genderWithConst = formWidget.fields().field("Gender with const").control(InputSelect.class);
        gender.shouldExists();
        gender.shouldHaveValue("Мужской");
        gender.openPopup();
        gender.dropdown().shouldHaveOptions(new String[]{"Мужской"});
        genderWithConst.shouldExists();
        genderWithConst.shouldHaveValue("Женский");
        genderWithConst.openPopup();
        genderWithConst.dropdown().shouldHaveOptions(new String[]{"Женский"});
    }

    @Test
    void testSearchMinLength() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/throttle_delay");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/throttle_delay/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/throttle_delay/test.query.xml")
        );

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelect = simplePage.widget(FormWidget.class)
                .fields().field("Input-select min-length=3").control(InputSelect.class);
        inputSelect.openPopup();
        inputSelect.shouldHaveDropdownMessage("Введите не менее 3 символов");
        DropDown dropdown = inputSelect.dropdown();
        dropdown.shouldHaveOptions(0);

        inputSelect.setValue("a");
        dropdown.shouldHaveOptions(0);

        inputSelect.setValue("au");
        dropdown.shouldHaveOptions(0);
        inputSelect.shouldHaveDropdownMessage("Введите не менее 3 символов");

        inputSelect.setValue("aud");
        dropdown.shouldHaveOptions(1);

        inputSelect.setValue("mer");
        inputSelect.shouldHaveDropdownMessage("Ничего не найдено");

    }

    @Test
    void dropdownOptionsPermanent() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/dropdwon_options_permanent");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/dropdwon_options_permanent/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/dropdwon_options_permanent/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions()
                .region(0, SimpleRegion.class)
                .content()
                .widget(0, TableWidget.class);
        table.shouldExists();

        InputSelect inputSelect = table.filters()
                .fieldsets()
                .fieldset(0, SimpleFieldSet.class)
                .fields()
                .field("status")
                .control(InputSelect.class);

        inputSelect.openPopup();
        DropDown dropdown = inputSelect.dropdown();
        dropdown.shouldHaveOptions(new String[]{"Новая", "Средняя", "Старая"});
        dropdown.selectItemBy(Condition.text("Средняя"));
        inputSelect.shouldHaveValue("Средняя");

        inputSelect.openPopup();
        dropdown.shouldHaveOptions(new String[]{"Новая", "Средняя", "Старая"});
    }

    @Test
    void inputUsingEnter() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/input_using_enter");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/input_using_enter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/input_using_enter/data.query.xml")
        );

        SimplePage page = open(SimplePage.class);

        FormWidget formWidget = page.widget(FormWidget.class);
        InputSelect inputSelect = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Ввод с выпадающим списком").control(InputSelect.class);
        inputSelect.click();
        inputSelect.shouldBeOpened();
        inputSelect.dropdown().shouldHaveOptions(new String[]{"111"});
        inputSelect.pressEnter();
        formWidget.toolbar().topLeft().button("unselect").click();
        inputSelect.shouldHaveValue("111");
    }

    @Test
    void testMaxTagsCount() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/max_count");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/input_select/max_count/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/max_count/test.query.xml"));

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelectUnlCheck = simplePage.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class)
                .fields().field("checkboxes").control(InputSelect.class);
        InputSelect inputSelectUnlMulti = simplePage.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class)
                .fields().field("multi").control(InputSelect.class);
        InputSelect inputSelectSingleCheck = simplePage.widget(FormWidget.class).fieldsets().fieldset(1, SimpleFieldSet.class)
                .fields().field("checkboxes").control(InputSelect.class);
        InputSelect inputSelectSingleMulti = simplePage.widget(FormWidget.class).fieldsets().fieldset(1, SimpleFieldSet.class)
                .fields().field("multi").control(InputSelect.class);
        InputSelect inputSelectFewCheck = simplePage.widget(FormWidget.class).fieldsets().fieldset(2, SimpleFieldSet.class)
                .fields().field("checkboxes").control(InputSelect.class);
        InputSelect inputSelectFewMulti = simplePage.widget(FormWidget.class).fieldsets().fieldset(2, SimpleFieldSet.class)
                .fields().field("multi").control(InputSelect.class);


        inputSelectUnlCheck.click();
        inputSelectUnlCheck.dropdown().selectMulti(2, 3, 4, 5, 6, 7, 8, 9);
        inputSelectUnlCheck.shouldSelectedMultiSize(8);
        inputSelectUnlCheck.click();
        inputSelectUnlMulti.click();
        inputSelectUnlMulti.dropdown().selectMulti(2, 3, 4, 5, 6, 7, 8, 9);
        inputSelectUnlMulti.shouldSelectedMultiSize(8);
        inputSelectUnlMulti.click();


        inputSelectSingleCheck.shouldSelectedMultiSize(1);
        inputSelectSingleCheck.shouldSelectedMulti(new String[]{"Выбрано: 8"});
        inputSelectSingleMulti.shouldSelectedMultiSize(1);
        inputSelectSingleCheck.shouldSelectedMulti(new String[]{"Выбрано: 8"});

        inputSelectFewCheck.shouldSelectedMultiSize(4);
        inputSelectFewCheck.shouldSelectedMulti(new String[]{"test3", "test4", "test5", "+ 5..."});
        inputSelectFewMulti.shouldSelectedMultiSize(4);
        inputSelectFewCheck.shouldSelectedMulti(new String[]{"test3", "test4", "test5", "+ 5..."});
    }

    @Test
    void testSearchFilterIdWithScrollDown() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/scroll");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/scroll/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/scroll/test.query.xml")
        );

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelect = simplePage.widget(FormWidget.class).fields().field("input").control(InputSelect.class);
        inputSelect.openPopup();
        DropDown dropdown = inputSelect.dropdown();

        inputSelect.setValue("1");
        dropdown.shouldHaveOptions(3);
        dropdown.shouldHaveOptions(new String[]{"Первый", "Десятый", "Одиннадцатый"});

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(6);

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(9);

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(12);

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(12);
        dropdown.shouldHaveOptions(new String[]{"Первый", "Десятый", "Одиннадцатый", "Двенадцатый", "Тринадцатый", "Четырнадцатый", "Пятнадцатый", "Шестнадцатый", "Семнадцатый", "Восемнадцатый", "Девятнадцатый", "Двадцать первый"});

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(12);
    }

    @Test
    void testInputSelectWithDatasourceAndScrollDown() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/datasource_scroll");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/datasource_scroll/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/datasource_scroll/test.query.xml")
        );

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelect = simplePage.widget(FormWidget.class).fields().field("select").control(InputSelect.class);
        inputSelect.openPopup();
        DropDown dropdown = inputSelect.dropdown();

        dropdown.shouldHaveOptions(7);

        dropdown.scrollDown();
        dropdown.shouldHaveOptions(14);

        dropdown.shouldHaveOptions(new String[]{
                "Первый", "Второй", "Третий", "Четвертый", "Пятый", "Шестой", "Седьмой",
                "Восьмой", "Девятый", "Десятый", "Одиннадцатый", "Двенадцатый", "Тринадцатый", "Четырнадцатый"
        });
    }

    @Test
    void testDatasourceListFiltering() {
        setResourcePath("net/n2oapp/framework/autotest/control/input_select/datasource_list_filtering");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/datasource_list_filtering/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/datasource_list_filtering/test.query.xml")
        );

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelect = simplePage.widget(FormWidget.class).fields().field("items").control(InputSelect.class);
        inputSelect.openPopup();
        DropDown dropdown = inputSelect.dropdown();

        dropdown.shouldHaveOptions(4);
        dropdown.shouldHaveOptions(new String[]{"test1", "test2", "test3", "test4"});

        inputSelect.setValue("2");
        dropdown.shouldHaveOptions(1);
        dropdown.shouldHaveOptions(new String[]{"test2"});
        inputSelect.clear();

        dropdown.shouldHaveOptions(4);
        dropdown.shouldHaveOptions(new String[]{"test1", "test2", "test3", "test4"});
    }
}