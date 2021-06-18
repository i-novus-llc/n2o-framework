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

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/date_interval/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        page = open(SimplePage.class);
        page.shouldExists();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void testDateInterval() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval1")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeEmpty();
        dateInterval.shouldBeCollapsed();
        dateInterval.expand();
        dateInterval.shouldBeExpanded();
        dateInterval.beginVal("12.02.2020");
        dateInterval.beginShouldHaveValue("12.02.2020");
        dateInterval.endVal("15.02.2020");
        dateInterval.endShouldHaveValue("15.02.2020");
        // проверка, что активные дни выставлены верно
        dateInterval.clickCalendarButton();
        dateInterval.shouldBeBeginActiveDay("12");
        dateInterval.shouldBeEndActiveDay("15");
        // проверка, что всегда begin <= end
        dateInterval.shouldBeDisableEndDay("11");
        dateInterval.shouldBeEnableEndDay("12");
        dateInterval.clickBeginDay("13");
        dateInterval.beginShouldHaveValue("13.02.2020");
        dateInterval.shouldBeDisableEndDay("12");
        // проверка, что при begin > end, значение end стирается
        dateInterval.clickBeginDay("16");
        dateInterval.endShouldBeEmpty();
        dateInterval.clickEndDay("18");
        dateInterval.endShouldHaveValue("18.02.2020");
        // проверка значений месяцев / годов
        dateInterval.beginShouldHaveCurrentMonth("Февраль");
        dateInterval.endShouldHaveCurrentMonth("Февраль");
        dateInterval.beginShouldHaveCurrentYear("2020");
        dateInterval.endShouldHaveCurrentYear("2020");
        // переход по месяцам, выборка даты из другого месяца/года
        dateInterval.clickBeginPreviousMonthButton();
        dateInterval.clickBeginPreviousMonthButton();
        dateInterval.beginShouldHaveCurrentMonth("Декабрь");
        dateInterval.beginShouldHaveCurrentYear("2019");
        dateInterval.clickBeginDay("25");
        dateInterval.beginShouldHaveValue("25.12.2019");
        dateInterval.clickEndNextMonthButton();
        dateInterval.clickEndDay("1");
        dateInterval.endShouldHaveCurrentMonth("Март");
        dateInterval.endShouldHaveValue("01.03.2020");
    }

    @Test
    public void testDateTimeInterval() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval2")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeEmpty();
        dateInterval.shouldBeCollapsed();
        dateInterval.expand();
        dateInterval.shouldBeExpanded();
        dateInterval.beginVal("12/02/2020 08:20:15");
        dateInterval.beginShouldHaveValue("12/02/2020 08:20:15");
        dateInterval.endVal("15/02/2020 12:34:56");
        dateInterval.endShouldHaveValue("15/02/2020 12:34:56");
        // задание времени
        dateInterval.beginTimeVal("1", "5", "9");
        dateInterval.beginShouldHaveValue("12/02/2020 01:05:09");
        dateInterval.endTimeVal("23", "59", "58");
        dateInterval.endShouldHaveValue("15/02/2020 23:59:58");
    }

    @Test
    public void testDateIntervalMaxMin() {
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("DateInterval3")
                .control(DateInterval.class);
        dateInterval.shouldExists();

        dateInterval.shouldBeCollapsed();
        dateInterval.expand();
        dateInterval.shouldBeExpanded();
        // проверка, что значения, выходящие за границы min/max, не вводятся
        dateInterval.beginVal("09.02.2020");
        dateInterval.beginShouldBeEmpty();
        dateInterval.beginVal("21.02.2020");
        dateInterval.beginShouldBeEmpty();
        dateInterval.endVal("09.02.2020");
        dateInterval.endShouldBeEmpty();
        dateInterval.endVal("21.02.2020");
        dateInterval.endShouldBeEmpty();
        dateInterval.beginVal("10.02.2020");
        dateInterval.beginShouldHaveValue("10.02.2020");
        dateInterval.endVal("20.02.2020");
        dateInterval.endShouldHaveValue("20.02.2020");
        // проверка, что значения, выходящие за границы min/max, нельзя выбрать в календаре
        dateInterval.clickCalendarButton();
        dateInterval.shouldBeDisableBeginDay("9");
        dateInterval.shouldBeEnableBeginDay("10");
        dateInterval.shouldBeEnableBeginDay("20");
        dateInterval.shouldBeDisableBeginDay("21");
        dateInterval.shouldBeDisableEndDay("9");
        dateInterval.shouldBeEnableEndDay("10");
        dateInterval.shouldBeEnableEndDay("20");
        dateInterval.shouldBeDisableEndDay("21");
    }
}
