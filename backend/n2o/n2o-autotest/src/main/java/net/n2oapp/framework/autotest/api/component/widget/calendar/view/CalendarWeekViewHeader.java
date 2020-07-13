package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

/**
 * Заголовок отображения календаря 'Неделя' для автотестирования
 */
public interface CalendarWeekViewHeader extends CalendarTimeViewHeader {
    void clickAllDay();

    void clickDayCell(String day);
}
