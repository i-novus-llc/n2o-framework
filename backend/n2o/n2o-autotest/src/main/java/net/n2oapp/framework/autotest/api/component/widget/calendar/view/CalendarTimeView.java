package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

/**
 * Стандартный вид отображений календаря со временем для автотестирования
 */
public interface CalendarTimeView extends StandardCalendarView {
    <T extends CalendarTimeViewHeader> T header(int index, Class<T> componentClass);
}
