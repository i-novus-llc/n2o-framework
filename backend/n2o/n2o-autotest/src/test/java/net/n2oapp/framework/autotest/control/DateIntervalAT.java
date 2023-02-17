package net.n2oapp.framework.autotest.control;

import net.n2oapp.framework.autotest.api.component.control.DateInterval;
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
 * Автотест компонента ввода интервала дат
 */
public class DateIntervalAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
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
                new CompileInfo("net/n2oapp/framework/autotest/control/date_interval/index.page.xml"));
    }

    @Test
    public void testDateInterval() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval1")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeEmpty();
        dateInterval.shouldBeClosed();
        dateInterval.openPopup();
        dateInterval.shouldBeOpened();
        dateInterval.setValueInBegin("12.02.2020");
        dateInterval.shouldHaveBeginValue("12.02.2020");
        dateInterval.setValueInEnd("15.02.2020");
        dateInterval.shouldHaveEndValue("15.02.2020");
        // проверка, что активные дни выставлены верно
        dateInterval.clickCalendarButton();
        dateInterval.shouldHaveBeginActiveDay("12");
        dateInterval.shouldHaveEndActiveDay("15");
        // проверка, что всегда begin <= end
        dateInterval.shouldHaveEndDisabledDay("11");
        dateInterval.shouldHaveEndEnabledDay("12");
        dateInterval.clickBeginDay("13");
        dateInterval.shouldHaveBeginValue("13.02.2020");
        dateInterval.shouldHaveEndDisabledDay("12");
        // проверка, что при begin > end, значение end стирается
        dateInterval.clickBeginDay("16");
        dateInterval.shouldHaveEmptyEnd();
        dateInterval.clickEndDay("18");
        dateInterval.shouldHaveEndValue("18.02.2020");
        // проверка значений месяцев / годов
        dateInterval.shouldHaveBeginCurrentMonth("Февраль");
        dateInterval.shouldHaveEndCurrentMonth("Февраль");
        dateInterval.shouldHaveBeginCurrentYear("2020");
        dateInterval.shouldHaveEndCurrentYear("2020");
        // переход по месяцам, выборка даты из другого месяца/года
        dateInterval.clickBeginPreviousMonthButton();
        dateInterval.clickBeginPreviousMonthButton();
        dateInterval.shouldHaveBeginCurrentMonth("Декабрь");
        dateInterval.shouldHaveBeginCurrentYear("2019");
        dateInterval.clickBeginDay("25");
        dateInterval.shouldHaveBeginValue("25.12.2019");
        dateInterval.clickEndNextMonthButton();
        dateInterval.clickEndDay("1");
        dateInterval.shouldHaveEndCurrentMonth("Март");
        dateInterval.shouldHaveEndValue("01.03.2020");
    }

    @Test
    public void testDateTimeInterval() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval2")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeEmpty();
        dateInterval.shouldBeClosed();
        dateInterval.openPopup();
        dateInterval.shouldBeOpened();
        dateInterval.setValueInBegin("12/02/2020 08:20:15");
        dateInterval.shouldHaveBeginValue("12/02/2020 08:20:15");
        dateInterval.setValueInEnd("15/02/2020 12:34:56");
        dateInterval.shouldHaveEndValue("15/02/2020 12:34:56");
        // задание времени
        dateInterval.setBeginTimeValue("1", "5", "9");
        dateInterval.shouldHaveBeginValue("12/02/2020 01:05:09");
        dateInterval.setEndTimeValue("23", "59", "58");
        dateInterval.shouldHaveEndValue("15/02/2020 23:59:58");
    }

    @Test
    public void testDateIntervalMaxMin() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval3")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeClosed();
        dateInterval.openPopup();
        dateInterval.shouldBeOpened();
        // проверка, что значения, выходящие за границы min/max, не вводятся
        dateInterval.setValueInBegin("09.02.2020");
        dateInterval.shouldHaveEmptyBegin();
        dateInterval.setValueInBegin("21.02.2020");
        dateInterval.shouldHaveEmptyBegin();
        dateInterval.setValueInEnd("09.02.2020");
        dateInterval.shouldHaveEmptyEnd();
        dateInterval.setValueInEnd("21.02.2020");
        dateInterval.shouldHaveEmptyEnd();
        dateInterval.setValueInBegin("10.02.2020");
        dateInterval.shouldHaveBeginValue("10.02.2020");
        dateInterval.setValueInEnd("20.02.2020");
        dateInterval.shouldHaveEndValue("20.02.2020");
        // проверка, что значения, выходящие за границы min/max, нельзя выбрать в календаре
        dateInterval.clickCalendarButton();
        dateInterval.shouldHaveBeginDisabledDay("9");
        dateInterval.shouldHaveBeginEnabledDay("10");
        dateInterval.shouldHaveBeginEnabledDay("20");
        dateInterval.shouldHaveBeginDisabledDay("21");
        dateInterval.shouldHaveEndDisabledDay("9");
        dateInterval.shouldHaveEndEnabledDay("10");
        dateInterval.shouldHaveEndEnabledDay("20");
        dateInterval.shouldHaveEndDisabledDay("21");
    }
}
