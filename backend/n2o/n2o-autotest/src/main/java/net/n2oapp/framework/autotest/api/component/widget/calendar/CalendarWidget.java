package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.widget.Widget;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarAgendaView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarDayView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarMonthView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarWeekView;

/**
 * Виджет календарь для автотестирования
 */
public interface CalendarWidget extends Widget {
    CalendarToolbar toolbar();

    CalendarMonthView monthView();

    CalendarDayView dayView();

    CalendarAgendaView agendaView();

    CalendarWeekView weekView();
}
