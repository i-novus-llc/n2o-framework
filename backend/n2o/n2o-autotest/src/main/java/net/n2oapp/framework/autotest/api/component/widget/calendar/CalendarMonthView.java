package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Вид отображения календаря 'Месяц' для автотестирования
 */
public interface CalendarMonthView extends Component {
    void shouldBeDayOff(String day);

    void shouldBeToday(String day);

    void clickOnCell(String day);

    void clickOnDay(String day);

    CalendarEvent event(String label);
}
