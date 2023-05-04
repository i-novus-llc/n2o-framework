package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест компонента выбора из выпадающего списка
 */
public class SelectAT extends AutoTestBase {

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
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
    }

    @Test
    public void testSelect() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/simple/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Select input = fields.field("Select1").control(Select.class);
        input.shouldExists();

        // close popup by icon
        input.openPopup();
        input.shouldBeOpened();
        input.closePopup();
        input.shouldBeClosed();

        // close popup by click on input area
        input.click();
        input.shouldBeOpened();
        input.click();
        input.shouldBeClosed();

        input.openPopup();
        DropDown dropdown = input.dropdown();
        dropdown.shouldHaveOptions("One", "Two", "Three");
        input.shouldBeEmpty();
        dropdown.selectItem(1);
        input.shouldSelected("Two");
        input.shouldBeCleanable();
        input.clear();
        input.shouldBeEmpty();

        Select input2 = fields.field("Select2").control(Select.class);
        input2.shouldExists();

        input2.openPopup();
        DropDown dropdown2 = input2.dropdown();
        dropdown2.selectItem(1);
        input2.shouldNotBeCleanable();

        // close popup by click on outside area
        input2.click();
        input2.shouldBeOpened();
        input.click();
        input2.shouldBeClosed();
    }

    @Test
    public void testCheckboxesType() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/checkboxes/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Select input = page.widget(FormWidget.class).fields().field("Select1")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        DropDown dropdown = input.dropdown();
        dropdown.shouldHaveOptions("One", "Two", "Three");
        dropdown.selectMulti(0);
        dropdown.shouldBeChecked(0);
        input.shouldSelected("Объектов 1 шт");
        dropdown.selectMulti(1, 2);
        dropdown.shouldBeChecked(0, 1, 2);
        input.shouldSelected("Объектов 3 шт");
        input.clear();
        dropdown.shouldNotBeChecked(0, 1, 2);
        input.shouldBeEmpty();
    }

    @Test
    public void testSelectFormat() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/format/index.page.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Select input = page.widget(FormWidget.class).fields().field("Select1")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        DropDown dropdown = input.dropdown();
        dropdown.shouldHaveOptions("One", "Two", "Three", "Four", "Five");
        dropdown.selectMulti(0);
        dropdown.shouldBeChecked(0);
        input.shouldSelected("1 объект");
        dropdown.selectMulti(1);
        dropdown.shouldBeChecked(0, 1);
        input.shouldSelected("2 объекта");
        dropdown.selectMulti(2, 3, 4);
        dropdown.shouldBeChecked(0, 1, 2, 3, 4);
        input.shouldSelected("5 объектов");
        input.clear();
        dropdown.shouldNotBeChecked(0, 1, 2, 3, 4);
        input.shouldBeEmpty();


        input = page.widget(FormWidget.class).fields().field("Select2")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        dropdown = input.dropdown();
        dropdown.shouldBeVisible();
        dropdown.selectMulti(0);
        input.shouldSelected("Объектов 1 шт");
        dropdown.selectMulti(1);
        input.shouldSelected("Объектов 2 шт");
        dropdown.selectMulti(2, 3, 4);
        input.shouldSelected("Объектов 5 шт");
        input.clear();
        input.shouldBeEmpty();
    }

    @Test
    public void testReadFromQuery() {
        setJsonPath("net/n2oapp/framework/autotest/control/select/query");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/select/query/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/select/query/test.query.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Select input = page.widget(FormWidget.class).fields().field("Select1")
                .control(Select.class);
        input.shouldExists();

        input.shouldBeEmpty();
        input.openPopup();
        DropDown dropdown = input.dropdown();
        dropdown.shouldHaveOptions("name1", "name2", "name3");
        input.openPopup();
        dropdown.item("name1").shouldHaveDescription("desc1");
        input.openPopup();
        dropdown.item("name3").shouldHaveDescription("desc3");
        dropdown.selectItem(1);
        input.shouldSelected("name2");
        input.clear();
        input.shouldBeEmpty();
        // сворачиваем popup, чтобы не накладывался на нижний контрол
        input.closePopup();


        Select input2 = page.widget(FormWidget.class).fields().field("Select2")
                .control(Select.class);
        input2.shouldExists();

        input2.shouldBeEmpty();
        input2.openPopup();
        DropDown dropdown2 = input2.dropdown();
        dropdown2.shouldHaveOptions("name1", "name2", "name3");
        input2.openPopup();
        dropdown2.item("name1").shouldHaveDescription("desc1");
        input2.openPopup();
        dropdown2.item("name3").shouldHaveDescription("desc3");
        dropdown2.selectMulti(1, 2);
        dropdown2.shouldBeChecked(1, 2);
        input2.shouldSelected("Объектов 2 шт");
        input2.clear();
        input2.shouldBeEmpty();
    }
}
