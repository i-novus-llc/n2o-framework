package net.n2oapp.framework.autotest.condition.field.dependency.reset;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест для reset зависимости полей
 */
class FieldResetDependencyAT extends AutoTestBase {

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
    void test() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/reset/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText reset = page.widget(FormWidget.class).fields().field("test").control(InputText.class);
        InputText inputText = page.widget(FormWidget.class).fields().field("input").control(InputText.class);
        DateInput datePicker = page.widget(FormWidget.class).fields().field("dateTime").control(DateInput.class);
        Select select = page.widget(FormWidget.class).fields().field("select").control(Select.class);
        InputSelect inputSelect = page.widget(FormWidget.class).fields().field("inputSelect").control(InputSelect.class);
        Checkbox checkbox = page.widget(FormWidget.class).fields().field("check").control(Checkbox.class);
        CheckboxGroup checkboxGroup = page.widget(FormWidget.class).fields().field("chgroup").control(CheckboxGroup.class);

        reset.shouldBeEmpty();
        inputText.shouldBeEmpty();
        inputText.click();
        inputText.setValue("test reset");
        datePicker.shouldBeEmpty();
        datePicker.setValue("02.02.2023");
        select.shouldBeEmpty();
        select.openPopup();
        select.dropdown().selectItem(1);
        inputSelect.shouldBeEmpty();
        inputSelect.click();
        inputSelect.openPopup();
        inputSelect.dropdown().selectMulti(1, 2);
        inputSelect.closePopup();
        checkbox.setChecked(true);
        checkboxGroup.check("test1");
        checkboxGroup.check("test3");

        checkInputText(inputText, datePicker, select, inputSelect, checkbox, checkboxGroup);

        reset.click();
        reset.setValue("2");
        checkInputText(inputText, datePicker, select, inputSelect, checkbox, checkboxGroup);

        reset.click();
        reset.setValue("1");
        inputText.shouldBeEmpty();
        datePicker.shouldBeEmpty();
        select.shouldBeEmpty();
        inputSelect.shouldBeEmpty();
        checkbox.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();
    }

    private static void checkInputText(InputText inputText, DateInput datePicker, Select select, InputSelect inputSelect, Checkbox checkbox, CheckboxGroup checkboxGroup) {
        inputText.shouldHaveValue("test reset");
        datePicker.shouldHaveValue("02.02.2023");
        select.shouldSelected("2");
        inputSelect.shouldSelectedMulti(new String[]{"2", "3"});
        checkbox.shouldBeChecked();
        checkboxGroup.shouldBeChecked("test1");
        checkboxGroup.shouldBeChecked("test3");
    }

    @Test
    void testApplyOnInit() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/reset_on_init/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText reset = page.widget(FormWidget.class).fields().field("reset").control(InputText.class);
        InputText initTrue = page.widget(FormWidget.class).fields().field("initTrue").control(InputText.class);
        InputText initFalse = page.widget(FormWidget.class).fields().field("initFalse").control(InputText.class);

        reset.shouldHaveValue("test");
        initTrue.shouldBeEmpty();
        initFalse.shouldHaveValue("test");
        reset.click();
        reset.clear();
        reset.setValue("test");
        initFalse.shouldBeEmpty();
    }

    @Test
    void testApplyOnInitShowModal() {
        setResourcePath("net/n2oapp/framework/autotest/condition/field/dependency/apply_on_init_modal");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/apply_on_init_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/apply_on_init_modal/test.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/apply_on_init_modal/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Modal modal = N2oSelenide.modal();
        page.widget(FormWidget.class).toolbar().topLeft().button("Модальное окно").click();
        modal.shouldExists();
        InputText name = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("name").control(InputText.class);
        InputText price = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("price").control(InputText.class);
        InputText defaultValue = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("defaultValue").control(InputText.class);
        InputText initValue = modal.content(SimplePage.class).widget(FormWidget.class).fields().field("initValue").control(InputText.class);

        name.shouldHaveValue("Audi");
        price.shouldHaveValue("60000");
        defaultValue.shouldHaveValue("default value");
        initValue.shouldHaveValue("apply on init");

        name.setValue("BMW");
        price.shouldBeEmpty();
        defaultValue.shouldHaveValue("on name");
        initValue.shouldHaveValue(  "on name");
    }
}
