package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarWeekViewHeader;

/**
 * Заголовок отображения календаря 'Неделя' для автотестирования
 */
public class N2oCalendarWeekViewHeader extends N2oCalendarTimeViewHeader implements CalendarWeekViewHeader {

    @Override
    public void clickAllDay() {
        // нельзя кликать для конкретного дня
        element().$(".rbc-allday-cell .rbc-row-content").click();
    }

    @Override
    public void clickDayCell(String day) {
        for (SelenideElement elm : element().$$(".rbc-time-header-cell .rbc-header"))
            if (elm.getText().contains(day)) {
                elm.click();
                break;
            }
    }
}
