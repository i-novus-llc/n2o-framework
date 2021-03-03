package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.IntervalField;
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
 * Автотест компонента интервала
 */
public class IntervalFieldAT extends AutoTestBase {

    @BeforeAll
    public static void BeforeAll() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/interval_field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testInput() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        IntervalField interval = page.widget(FormWidget.class).fields().field("Интервал", IntervalField.class);
        InputText inputBegin = interval.begin(InputText.class);
        InputText inputEnd = interval.end(InputText.class);

        inputBegin.shouldHaveValue("5");
        inputBegin.clickPlusStepButton();
        inputBegin.shouldHaveValue("6");

        inputEnd.shouldHaveValue("10");
        inputEnd.clickPlusStepButton();
        inputEnd.shouldHaveValue("10");
        inputEnd.clickMinusStepButton();
        inputEnd.shouldHaveValue("9");
    }

    @Test
    public void testIntervalWithDate() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        IntervalField interval = page.widget(FormWidget.class).fields().field("Дата", IntervalField.class);
        DateInput beginDate = interval.begin(DateInput.class);
        DateInput endDate = interval.end(DateInput.class);

        beginDate.shouldHaveValue("21.11.1999");
        endDate.shouldHaveValue("");
        endDate.val("29042020");
        endDate.shouldHaveValue("29.04.2020");
        endDate.shouldBeActiveDay("29");
        endDate.clickNextMonthButton();
        endDate.shouldHaveCurrentMonth("Май");
        endDate.clickDay("15");
        endDate.shouldHaveValue("15.05.2020");
    }
}
