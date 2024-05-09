package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.ClickOptions;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarDayView;

/**
 * Вид отображения календаря 'День' для автотестирования
 */
public class N2oCalendarDayView extends N2oCalendarTimeView implements CalendarDayView {

    @Override
    public void clickCell(int resourceIndex, String startTime) {
        String[] time = startTime.split(":");
        int cellIdx = Integer.parseInt(time[0]) * 2;
        if ("30".equals(time[1]))
            cellIdx++;

        element().$$(".rbc-time-content .rbc-day-slot").get(resourceIndex)
                .$$(".calendar__cell").get(cellIdx).click(ClickOptions.usingDefaultMethod());
    }

    @Override
    public void clickCell(String startTime) {
        clickCell(0, startTime);
    }
}
