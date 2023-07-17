package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

import java.time.Duration;

/**
 * Вид отображения календаря 'Месяц' для автотестирования
 */
public interface CalendarMonthView extends StandardCalendarView {
    void shouldBeDayOff(String day);

    void shouldBeToday(String day, Duration... duration);

    void clickOnCell(String day);

    void clickOnDay(String day);
}
