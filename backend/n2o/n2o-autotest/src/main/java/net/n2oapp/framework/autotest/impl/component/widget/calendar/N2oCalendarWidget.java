package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarAgendaView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarMonthView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarToolbar;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarWidget;
import net.n2oapp.framework.autotest.impl.component.widget.N2oWidget;

/**
 * Виджет календарь для автотестирования
 */
public class N2oCalendarWidget extends N2oWidget implements CalendarWidget {
    @Override
    public CalendarToolbar toolbar() {
        return N2oSelenide.component(element().$(".rbc-toolbar"), CalendarToolbar.class);
    }

    @Override
    public CalendarMonthView monthView() {
        return N2oSelenide.component(element().$(".rbc-month-view"), CalendarMonthView.class);
    }

    @Override
    public CalendarAgendaView agendaView() {
        return N2oSelenide.component(element().$(".rbc-agenda-view"), CalendarAgendaView.class);
    }
}
