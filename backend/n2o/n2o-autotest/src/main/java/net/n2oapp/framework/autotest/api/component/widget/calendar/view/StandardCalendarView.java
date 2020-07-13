package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarEvent;

/**
 * Стандартный вид отображения календаря для автотестирования
 */
public interface StandardCalendarView extends Component {
    CalendarEvent event(String label);
}
