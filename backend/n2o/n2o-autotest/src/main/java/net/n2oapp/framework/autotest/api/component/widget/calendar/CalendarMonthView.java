package net.n2oapp.framework.autotest.api.component.widget.calendar;

/**
 * Вид отображения календаря 'Месяц' для автотестирования
 */
public interface CalendarMonthView extends StandardCalendarView {
    void shouldBeDayOff(String day);

    void shouldBeToday(String day);

    void clickOnCell(String day);

    void clickOnDay(String day);
}
