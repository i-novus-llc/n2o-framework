package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarTimeViewHeader;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Заголовок отображения календаря со временем для автотестирования
 */
public abstract class N2oCalendarTimeViewHeader extends N2oComponent implements CalendarTimeViewHeader {
    @Override
    public void shouldHaveTitle(String title, Duration... duration) {
        should(Condition.text(title), element().$(".rbc-row-resource .rbc-header"), duration);
    }

    @Override
    public CalendarEvent allDayEvent(String label) {
        return N2oSelenide.component(
                element().$$(".rbc-allday-cell .calendar__event .calendar__event-name").find(Condition.text(label)).parent(),
                CalendarEvent.class);
    }
}
