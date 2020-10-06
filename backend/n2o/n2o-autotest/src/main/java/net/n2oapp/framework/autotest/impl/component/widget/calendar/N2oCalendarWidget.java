package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarToolbar;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarWidget;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarAgendaView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarDayView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarMonthView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarWeekView;
import net.n2oapp.framework.autotest.impl.component.widget.N2oStandardWidget;

/**
 * Виджет календарь для автотестирования
 */
public class N2oCalendarWidget extends N2oStandardWidget implements CalendarWidget {
    @Override
    public CalendarToolbar calendarToolbar() {
        return N2oSelenide.component(element().$(".rbc-toolbar"), CalendarToolbar.class);
    }

    @Override
    public CalendarMonthView monthView() {
        return N2oSelenide.component(element().$(".rbc-month-view"), CalendarMonthView.class);
    }

    @Override
    public CalendarDayView dayView() {
        return N2oSelenide.component(element().$(".rbc-time-view"), CalendarDayView.class);
    }

    @Override
    public CalendarAgendaView agendaView() {
        return N2oSelenide.component(element().$(".rbc-agenda-view"), CalendarAgendaView.class);
    }

    @Override
    public CalendarWeekView weekView() {
        return N2oSelenide.component(element().$(".rbc-time-view"), CalendarWeekView.class);
    }
}
