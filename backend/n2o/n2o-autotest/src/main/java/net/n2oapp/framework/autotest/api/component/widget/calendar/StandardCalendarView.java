package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Стандартный вид отображения календаря для автотестирования
 */
public interface StandardCalendarView extends Component {
    CalendarEvent event(String label);
}
