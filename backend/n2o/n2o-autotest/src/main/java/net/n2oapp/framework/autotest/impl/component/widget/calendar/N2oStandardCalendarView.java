package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;
import net.n2oapp.framework.autotest.api.component.widget.calendar.StandardCalendarView;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Стандартный вид отображения календаря для автотестирования
 */
public abstract class N2oStandardCalendarView extends N2oComponent implements StandardCalendarView {
    @Override
    public CalendarEvent event(String label) {
        return N2oSelenide.component(
                element().$$(".calendar__event .calendar__event-name").find(Condition.text(label)).parent(),
                CalendarEvent.class);
    }
}
