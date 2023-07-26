package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;

import java.time.Duration;

/**
 * Заголовок отображения календаря со временем для автотестирования
 */
public interface CalendarTimeViewHeader extends Component {
    void shouldHaveTitle(String title, Duration... duration);

    CalendarEvent allDayEvent(String label);
}
