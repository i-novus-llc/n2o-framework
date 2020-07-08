package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarAgendaView;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public class N2oCalendarAgendaView extends N2oComponent implements CalendarAgendaView {

    @Override
    public TableWidget table() {
        return N2oSelenide.component(element().$(".rbc-agenda-table"), TableWidget.class);
    }
}
