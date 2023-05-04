package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
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
public class InputSelectAT extends AutoTestBase {

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
    public void testSingle() {
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
        input.dropdown().shouldHaveOptions("One", "Two", "Three");
        // close popup by click on options
        input.openPopup();
        input.dropdown().selectItem(1);
        input.shouldBeClosed();
        input.shouldHaveValue("Two");

        input.openPopup();
        input.dropdown().item(1).shouldBeSelected();

        input.clear();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().item(1).shouldNotBeSelected();

        input.setValue("Three");
        input.shouldHaveValue("Three");
        input.closePopup();

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
    public void testColorStatus() {
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
        dropdown.item("One").shouldHaveStatusColor(Colors.SUCCESS);
        dropdown.item("Two").shouldHaveStatusColor(Colors.PRIMARY);
        dropdown.item("Three").shouldHaveStatusColor(Colors.DANGER);
    }

    @Test
    public void testMulti() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/multi/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputSelect input = page.widget(FormWidget.class).fields().field("InputSelect")
                .control(InputSelect.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        input.dropdown().shouldHaveOptions("One", "Two", "Three");
        input.openPopup();
        input.dropdown().selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
        input.clearItems("Two", "One");
        input.shouldBeEmpty();
    }

    @Test
    public void testCheckboxes() {
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
        input.dropdown().shouldHaveOptions("One", "Two", "Three");
        input.openPopup();
        input.dropdown().selectMulti(1, 2);
        input.shouldSelectedMulti("Two", "Three");
        input.clear();
        input.shouldBeEmpty();

        input.openPopup();
        input.dropdown().selectMulti(2, 1, 0);
        input.shouldSelectedMulti("Three", "Two", "One");

        // удаление выбранных значений
        input.clearItems("Three");
        input.shouldSelectedMulti("Two", "One");
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
    public void testReadFromQuery() {
        setJsonPath("net/n2oapp/framework/autotest/control/input_select/query");
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
        dropdown.shouldHaveOptions("name1", "name2", "name3");
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
        dropdown2.shouldHaveOptions("name1", "name2", "name3");
        input2.openPopup();
        dropdown2.selectMulti(1, 2);
        input2.shouldSelectedMulti("name2", "name3");
        input2.clear();
        input2.shouldBeEmpty();
    }

    @Test
    public void testPrefilterByQueryParam() {
        setJsonPath("net/n2oapp/framework/autotest/control/input_select/prefilter");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_query_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/gender.query.xml")
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
        gender.dropdown().shouldHaveOptions("Мужской");
        genderWithConst.shouldExists();
        genderWithConst.shouldHaveValue("Женский");
        genderWithConst.openPopup();
        genderWithConst.dropdown().shouldHaveOptions("Женский");
    }

    @Test
    public void testPrefilterByPathParam() {
        setJsonPath("net/n2oapp/framework/autotest/control/input_select/prefilter");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/by_path_param/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/modalForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/prefilter/gender.query.xml")
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
        gender.dropdown().shouldHaveOptions("Мужской");
        genderWithConst.shouldExists();
        genderWithConst.shouldHaveValue("Женский");
        genderWithConst.openPopup();
        genderWithConst.dropdown().shouldHaveOptions("Женский");
    }

    @Test
    public void testSearchMinLength() {
        setJsonPath("net/n2oapp/framework/autotest/control/input_select/throttle_delay");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/throttle_delay/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/input_select/throttle_delay/test.query.xml")
        );

        SimplePage simplePage = open(SimplePage.class);
        simplePage.shouldExists();

        InputSelect inputSelect = simplePage.widget(FormWidget.class)
                .fields().field("Input-select min-length=3").control(InputSelect.class);
        inputSelect.openPopup();
        DropDown dropdown = inputSelect.dropdown();
        dropdown.shouldHaveOptions(3);

        inputSelect.setValue("a");
        dropdown.shouldHaveOptions(3);

        inputSelect.setValue("au");
        dropdown.shouldHaveOptions(3);

        inputSelect.setValue("aud");
        dropdown.shouldHaveOptions(1);
    }

    @Test
    public void dropdownOptionsPermanentAfterSelectSomeone() {
        setJsonPath("net/n2oapp/framework/autotest/control/input_select/dropdwon_options_permanent");
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
        dropdown.shouldHaveOptions("Новая", "Средняя", "Старая");
        dropdown.selectItemBy(Condition.text("Средняя"));
        inputSelect.shouldHaveValue("Средняя");

        inputSelect.openPopup();
        dropdown.shouldHaveOptions("Новая", "Средняя", "Старая");
    }
}
