package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

/**
 * Вид отображения календаря 'Неделя' для автотестирования
 */
public interface CalendarWeekView extends CalendarTimeView {
    void clickCell(int resourceIndex, String day, String startTime);
}
