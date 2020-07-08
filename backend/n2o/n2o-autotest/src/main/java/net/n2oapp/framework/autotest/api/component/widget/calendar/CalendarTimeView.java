package net.n2oapp.framework.autotest.api.component.widget.calendar;

/**
 * Вид отображения календаря 'День' для автотестирования
 */
public interface CalendarTimeView extends StandardCalendarView {
    CalendarTimeViewHeader header(int index);

    void clickCell(int columnIndex, String startDate);
}
