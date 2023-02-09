package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

import net.n2oapp.framework.autotest.api.component.Component;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public interface CalendarAgendaView extends Component {

    void shouldHaveSize(int size);

    void shouldHaveEventOnDate(int index, String date);

    void shouldHaveEventInTime(int index, String time);

    void shouldHaveEventWithName(int index, String name);
}
