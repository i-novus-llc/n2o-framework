package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

/**
 * Вид отображения календаря 'День' для автотестирования
 */
public interface CalendarDayView extends CalendarTimeView {
    void clickCell(int resourceIndex, String startTime);
}
