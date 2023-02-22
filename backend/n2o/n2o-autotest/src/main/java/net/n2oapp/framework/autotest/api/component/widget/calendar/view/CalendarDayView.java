package net.n2oapp.framework.autotest.api.component.widget.calendar.view;

/**
 * Вид отображения календаря 'День' для автотестирования
 */
public interface CalendarDayView extends CalendarTimeView {

    /**
     * Клик по ячейке с временем
     * @param resourceIndex строка времени
     * @param startTime время начала
     */
    void clickCell(int resourceIndex, String startTime);

    /**
     * Клик по ячейке с временем при resourceIndex=0
     */
    void clickCell(String startTime);
}
