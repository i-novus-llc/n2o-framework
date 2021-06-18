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
public class NumberPickerAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/number_picker/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testNumberPicker() {
        Fields fields = page.widget(FormWidget.class).fields();
        NumberPicker numberPicker = fields.field("limitedPicker").control(NumberPicker.class);
        numberPicker.shouldExists();
        numberPicker.shouldBeEnabled();
        numberPicker.minShouldBe("-3");
        numberPicker.maxShouldBe("3");
        numberPicker.stepShouldBe("2");
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
        numberPicker.val("-2");
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
        numberPicker.val("-");
        numberPicker.shouldHaveValue("-3");
        numberPicker.clear();
        // default min value
        numberPicker.shouldHaveValue("-3");
        numberPicker.val("10");
        // if more than max then set max value
        numberPicker.shouldHaveValue("3");
        numberPicker.val("-10");
        // if less than min then set min value
        numberPicker.shouldHaveValue("-3");
    }
}