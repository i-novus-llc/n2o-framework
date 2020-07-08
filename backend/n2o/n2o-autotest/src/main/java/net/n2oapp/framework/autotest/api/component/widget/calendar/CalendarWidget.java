package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.widget.Widget;

/**
 * Виджет календарь для автотестирования
 */
public interface CalendarWidget extends Widget {
    CalendarToolbar toolbar();

    CalendarMonthView monthView();

    CalendarTimeView dayView();

    CalendarAgendaView agendaView();
}
