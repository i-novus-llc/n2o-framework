package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;

import java.time.Duration;

/**
 * Событие календаря для автотестирования
 */
public interface CalendarEvent extends Component {
    void shouldHaveTooltipTitle(String title, Duration... duration);

    void shouldNotHaveTooltip();

    void click();
}
