package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Событие календаря для автотестирования
 */
public class N2oCalendarEvent extends N2oComponent implements CalendarEvent {

    @Override
    public void shouldHaveTooltipTitle(String title, Duration... duration) {
        should(Condition.attribute("title", title), duration);
    }

    @Override
    public void shouldNotHaveTooltip() {
        shouldHaveTooltipTitle("");
    }

    @Override
    public void click() {
        element().click();
    }
}
