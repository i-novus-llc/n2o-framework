package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarDayViewHeader;

/**
 * Заголовок отображения календаря 'День' для автотестирования
 */
public class N2oCalendarDayViewHeader extends N2oCalendarTimeViewHeader implements CalendarDayViewHeader {
    @Override
    public void clickAllDay() {
        element().$(".rbc-allday-cell").click();
    }
}
