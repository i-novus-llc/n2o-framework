package net.n2oapp.framework.autotest.control.condition;

import net.n2oapp.framework.autotest.api.component.control.*;
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
public class FieldResetDependencyAT extends AutoTestBase {

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
    public void test() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/reset/index.page.xml"));

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
        select.select(1);
        inputSelect.shouldBeEmpty();
        inputSelect.click();
        inputSelect.selectMulti(1, 2);
        inputSelect.closePopup();
        checkbox.setChecked(true);
        checkboxGroup.check("test1");
        checkboxGroup.check("test3");

        inputText.shouldHaveValue("test reset");
        datePicker.shouldHaveValue("02.02.2023");
        select.shouldSelected("2");
        inputSelect.shouldSelectedMulti("2", "3");
        checkbox.shouldBeChecked();
        checkboxGroup.shouldBeChecked("test1");
        checkboxGroup.shouldBeChecked("test3");

        reset.click();
        reset.setValue("2");
        inputText.shouldHaveValue("test reset");
        datePicker.shouldHaveValue("02.02.2023");
        select.shouldSelected("2");
        inputSelect.shouldSelectedMulti("2", "3");
        checkbox.shouldBeChecked();
        checkboxGroup.shouldBeChecked("test1");
        checkboxGroup.shouldBeChecked("test3");

        reset.click();
        reset.setValue("1");
        inputText.shouldBeEmpty();
        datePicker.shouldBeEmpty();
        select.shouldBeEmpty();
        inputSelect.shouldBeEmpty();
        checkbox.shouldBeEmpty();
        checkboxGroup.shouldBeEmpty();
    }

    @Test
    public void testApplyOnInit() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/condition/reset/init/index.page.xml"));

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
}
