package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.ClickOptions;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarWeekView;

/**
 * Вид отображения календаря 'Неделя' для автотестирования
 */
public class N2oCalendarWeekView extends N2oCalendarTimeView implements CalendarWeekView {
    @Override
    public void clickCell(int resourceIndex, String day, String startTime) {
        String[] time = startTime.split(":");
        int cellIdx = Integer.parseInt(time[0]) * 2;
        if ("30".equals(time[1]))
            cellIdx++;

        int dayOfWeekIndex = 0;
        for (SelenideElement elm : element().$$(".rbc-time-header-cell .rbc-header")) {
            if (elm.getText().contains(day))
                break;
            dayOfWeekIndex++;
        }

        element().$$(".rbc-time-content .rbc-day-slot").get(resourceIndex * 7 + dayOfWeekIndex)
                .$$(".calendar__cell").get(cellIdx).click(ClickOptions.usingDefaultMethod());
    }

    @Override
    public void clickCell(String day, String startTime) {
        clickCell(0, day, startTime);
    }
}
