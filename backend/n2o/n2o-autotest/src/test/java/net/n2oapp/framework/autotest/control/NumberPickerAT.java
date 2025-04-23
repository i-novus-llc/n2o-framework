package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.NumberPicker;
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
 * Автотест компонента ввода числа из диапазона
 */
class NumberPickerAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/number_picker/index.page.xml"));
    }

    @Test
    void testNumberPicker() {
        Fields fields = page.widget(FormWidget.class).fields();
        NumberPicker numberPicker = fields.field("limitedPicker").control(NumberPicker.class);
        numberPicker.shouldExists();
        numberPicker.shouldBeEnabled();
        numberPicker.shouldHaveMin("-3");
        numberPicker.shouldHaveMax("3");
        numberPicker.shouldHaveStep("2");
        numberPicker.minusStepButtonShouldBeEnabled();
        numberPicker.plusStepButtonShouldBeEnabled();
        numberPicker.shouldHaveValue("2");
        numberPicker.clickPlusStepButton();
        // limit by max value
        numberPicker.shouldHaveValue("3");
        numberPicker.minusStepButtonShouldBeEnabled();
        numberPicker.plusStepButtonShouldBeDisabled();
        numberPicker.clickMinusStepButton();
        numberPicker.shouldHaveValue("1");
        numberPicker.clickMinusStepButton();
        numberPicker.shouldHaveValue("-1");
        numberPicker.click();
        numberPicker.setValue("-2");
        numberPicker.shouldHaveValue("-2");
        numberPicker.clickMinusStepButton();
        // limit by min value
        numberPicker.shouldHaveValue("-3");
        numberPicker.minusStepButtonShouldBeDisabled();
        numberPicker.plusStepButtonShouldBeEnabled();
        numberPicker.clickPlusStepButton();
        numberPicker.shouldHaveValue("-1");
        numberPicker.minusStepButtonShouldBeEnabled();

        //check input
        // only minus
        numberPicker.click();
        numberPicker.setValue("-");
        numberPicker.shouldHaveValue("-3");
        numberPicker.click();
        numberPicker.clear();
        // default min value
        numberPicker.shouldHaveValue("-3");
        numberPicker.click();
        numberPicker.setValue("10");
        // if more than max then set max value
        numberPicker.shouldHaveValue("3");
        numberPicker.click();
        numberPicker.setValue("-10");
        // if less than min then set min value
        numberPicker.shouldHaveValue("-3");
    }
}