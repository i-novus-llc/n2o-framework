package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

/**
 * Автотест компонента ввода даты
 */
class DatePickerAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oApplicationPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/date_picker/simple/index.page.xml"));
    }

    @Test
    void testDatePicker() {
        DateInput date = page.widget(FormWidget.class).fields().field("Date1")
                .control(DateInput.class);
        date.shouldExists();
        date.shouldBeEmpty();
        date.shouldHavePlaceholder("Введите дату");
        date.shouldBeClosed();
        date.openPopup();
        date.shouldBeOpened();
        date.setValue("15.02.2020");
        date.shouldHaveValue("15.02.2020");
        date.clickCalendarButton();
        date.shouldBeActiveDay("15");
        // проверка, что значения, выходящие за границы min/max, скрыты
        date.shouldBeDisableDay("9");
        date.shouldNotBeDisableDay("10");
        date.shouldBeDisableDay("21");
        date.shouldNotBeDisableDay("20");
        // проверка клика по дню
        date.clickDay("12");
        date.shouldBeClosed();
        date.shouldHaveValue("12.02.2020");
        // проверка месяцев и переход к предыдущему/следующему месяцу
        date.clickCalendarButton();
        date.shouldBeOpened();
        date.shouldHaveCurrentMonth("Февраль");
        date.shouldHaveCurrentYear("2020");
        date.clickPreviousMonthButton();
        date.clickPreviousMonthButton();
        date.shouldHaveCurrentMonth("Декабрь");
        date.shouldHaveCurrentYear("2019");
        date.clickNextMonthButton();
        date.shouldHaveCurrentMonth("Январь");
        // проверка, что значения, выходящие за границы min/max, не вводятся
        date.setValue("05.02.2021");
        date.shouldHaveValue("12.02.2020");
        date.setValue("25.02.2020");
        date.shouldHaveValue("12.02.2020");
    }

    @Test
    void testDateTime() {
        DateInput date = page.widget(FormWidget.class).fields().field("Date2")
                .control(DateInput.class);
        date.shouldExists();
        date.shouldBeClosed();
        date.shouldBeEmpty();
        date.openPopup();
        date.shouldBeOpened();
        date.timeButtonShouldHaveLabel("Выберите время");
        date.clickDay("1");
        date.setValue("15/02/2020 10:20:15");
        date.shouldHaveValue("15/02/2020 10:20:15");
        date.clickCalendarButton();
        date.timeButtonShouldHaveLabel("10:20");
        date.timeVal("23", "59", "58");
        date.shouldHaveValue("15/02/2020 23:59:58");
    }

    @Test
    void testDefaultValue() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/date_picker/default/index.page.xml"));
        page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String month = addLeadingZero(calendar.get(Calendar.MONTH) + 1);
        String day = addLeadingZero(calendar.get(Calendar.DAY_OF_MONTH));

        DateInput today = fields.field("Сегодня").control(DateInput.class);
        today.shouldHaveValue(String.format("%s.%s.%s", day, month, year));

        DateInput beginMonth = fields.field("Начало месяца").control(DateInput.class);
        beginMonth.shouldHaveValue(String.format("01.%s.%s", month, year));

        int monthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        DateInput endMonth = fields.field("Конец месяца").control(DateInput.class);
        endMonth.shouldHaveValue(String.format("%s.%s.%s", monthLastDay, month, year));

        DateInput beginYear = fields.field("Начало года").control(DateInput.class);
        beginYear.shouldHaveValue("01.01." + year);

        DateInput endYear = fields.field("Конец года").control(DateInput.class);
        endYear.shouldHaveValue("31.12." + year);
    }

    private String addLeadingZero(int i) {
        return i < 10 ? "0" + i : "" + i;
    }
}
