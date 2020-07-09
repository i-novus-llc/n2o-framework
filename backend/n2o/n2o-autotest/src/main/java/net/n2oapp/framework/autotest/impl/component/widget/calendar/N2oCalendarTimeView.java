package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarTimeView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarTimeViewHeader;
import org.openqa.selenium.Point;

/**
 * Вид отображения календаря 'День' для автотестирования
 */
public class N2oCalendarTimeView extends N2oStandardCalendarView implements CalendarTimeView {
    @Override
    public CalendarTimeViewHeader header(int index) {
        return N2oSelenide.component(element().$$(".rbc-time-header-content").get(index), CalendarTimeViewHeader.class);
    }

    @Override
    public void clickCell(int columnIndex, String startDate) {
        String[] time = startDate.split(":");
        int cellIdx = Integer.parseInt(time[0]) * 2;
        if ("30".equals(time[1]))
            cellIdx++;

        // TODO
        Point location = element().$$(".rbc-time-content .rbc-day-slot").get(columnIndex)
                .$$(".calendar__cell").get(cellIdx).getLocation();
        element().$$(".rbc-events-container").get(columnIndex).click(location.getX(), location.getY());
    }
}
