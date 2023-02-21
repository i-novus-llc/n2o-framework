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
        dateInterval.beginShouldHaveValue("12.02.2020");
        dateInterval.setValueInEnd("15.02.2020");
        dateInterval.endShouldHaveValue("15.02.2020");
        // проверка, что активные дни выставлены верно
        dateInterval.clickCalendarButton();
        dateInterval.beginDayShouldBeActive("12");
        dateInterval.endDayShouldBeActive("15");
        // проверка, что всегда begin <= end
        dateInterval.endDayShouldBeDisabled("11");
        dateInterval.endDayShouldBeEnabled("12");
        dateInterval.clickBeginDay("13");
        dateInterval.beginShouldHaveValue("13.02.2020");
        dateInterval.endDayShouldBeDisabled("12");
        // проверка, что при begin > end, значение end стирается
        dateInterval.clickBeginDay("16");
        dateInterval.endShouldBeEmpty();
        dateInterval.clickEndDay("18");
        dateInterval.endShouldHaveValue("18.02.2020");
        // проверка значений месяцев / годов
        dateInterval.beginCurrentMonthShouldHaveValue("Февраль");
        dateInterval.endCurrentMonthShouldHaveValue("Февраль");
        dateInterval.beginCurrentYearShouldHaveValue("2020");
        dateInterval.endCurrentYearShouldHaveValue("2020");
        // переход по месяцам, выборка даты из другого месяца/года
        dateInterval.clickBeginMonthPreviousButton();
        dateInterval.clickBeginMonthPreviousButton();
        dateInterval.beginCurrentMonthShouldHaveValue("Декабрь");
        dateInterval.beginCurrentYearShouldHaveValue("2019");
        dateInterval.clickBeginDay("25");
        dateInterval.beginShouldHaveValue("25.12.2019");
        dateInterval.clickEndMonthNextButton();
        dateInterval.clickEndDay("1");
        dateInterval.endCurrentMonthShouldHaveValue("Март");
        dateInterval.endShouldHaveValue("01.03.2020");
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
        dateInterval.beginShouldHaveValue("12/02/2020 08:20:15");
        dateInterval.setValueInEnd("15/02/2020 12:34:56");
        dateInterval.endShouldHaveValue("15/02/2020 12:34:56");
        // задание времени
        dateInterval.beginTimeSetValue("1", "5", "9");
        dateInterval.beginShouldHaveValue("12/02/2020 01:05:09");
        dateInterval.endTimeSetValue("23", "59", "58");
        dateInterval.endShouldHaveValue("15/02/2020 23:59:58");
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
        dateInterval.beginShouldBeEmpty();
        dateInterval.setValueInBegin("21.02.2020");
        dateInterval.beginShouldBeEmpty();
        dateInterval.setValueInEnd("09.02.2020");
        dateInterval.endShouldBeEmpty();
        dateInterval.setValueInEnd("21.02.2020");
        dateInterval.endShouldBeEmpty();
        dateInterval.setValueInBegin("10.02.2020");
        dateInterval.beginShouldHaveValue("10.02.2020");
        dateInterval.setValueInEnd("20.02.2020");
        dateInterval.endShouldHaveValue("20.02.2020");
        // проверка, что значения, выходящие за границы min/max, нельзя выбрать в календаре
        dateInterval.clickCalendarButton();
        dateInterval.beginDayShouldBeDisabled("9");
        dateInterval.beginDayShouldBeEnabled("10");
        dateInterval.beginDayShouldBeEnabled("20");
        dateInterval.beginDayShouldBeDisabled("21");
        dateInterval.endDayShouldBeDisabled("9");
        dateInterval.endDayShouldBeEnabled("10");
        dateInterval.endDayShouldBeEnabled("20");
        dateInterval.endDayShouldBeDisabled("21");
    }
}
