package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.control.N2oDateInput;
import net.n2oapp.framework.autotest.impl.component.page.N2oSimplePage;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Автотест компонента ввода даты
 */
public class DatePickerAT extends AutoTestBase {

    private SimplePage page;

    @BeforeClass
    public static void beforeClass() {
        configureSelenide();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/date_picker/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(N2oSimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testDatePicker() {
        N2oDateInput date = page.single().widget(FormWidget.class).fields().field("Date1")
                .control(N2oDateInput.class);
        date.shouldExists();

        date.val("15.02.2020");
//        date.shouldHaveValue("15.02.2020");
        date.clickCalendarButton();
        date.shouldBeActiveDay("15");
        // проверка, что значения, выходящие за границы min/max, скрыты
        date.shouldBeDisableDay("9");
        date.shouldNotBeDisableDay("10");
        date.shouldBeDisableDay("21");
        date.shouldNotBeDisableDay("20");
        // проверка клика по дню
        date.clickDay("12");
        date.shouldHaveValue("12.02.2020");
        // проверка месяцев и переход к предыдущему/следующему месяцу
        date.clickCalendarButton();
        date.shouldHaveCurrentMonth("Февраль");
        date.shouldHaveCurrentYear("2020");
        date.clickPreviousMonthButton();
        date.clickPreviousMonthButton();
        date.shouldHaveCurrentMonth("Декабрь");
        date.shouldHaveCurrentYear("2019");
        date.clickNextMonthButton();
        date.shouldHaveCurrentMonth("Январь");
        // проверка, что значения, выходящие за границы min/max, не вводятся
        date.val("05.02.2020");
        date.shouldHaveValue("");
        date.val("25.02.2020");
        date.shouldHaveValue("");
    }

    @Test
    public void testDateTime() {
        N2oDateInput date = page.single().widget(FormWidget.class).fields().field("Date2")
                .control(N2oDateInput.class);
        date.shouldExists();

        date.shouldHaveValue("01/01/2020 00:00:00");
        date.val("15/02/2020 10:20:15");
        date.shouldHaveValue("15/02/2020 10:20:15");
        date.clickCalendarButton();
        date.timeVal("23", "59", "59");
        date.shouldHaveValue("15/02/2020 23:59:59");
    }
}
