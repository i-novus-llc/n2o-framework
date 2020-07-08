package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public interface CalendarAgendaView extends Component {
    TableWidget table();
}
