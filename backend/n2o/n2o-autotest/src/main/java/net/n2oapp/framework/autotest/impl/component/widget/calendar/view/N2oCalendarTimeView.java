package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarTimeView;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarTimeViewHeader;

/**
 * Стандартный вид отображений календаря со временем для автотестирования
 */
public abstract class N2oCalendarTimeView extends N2oStandardCalendarView implements CalendarTimeView {
    @Override
    public <T extends CalendarTimeViewHeader> T header(Class<T> componentClass) {
        return header(0, componentClass);
    }

    @Override
    public <T extends CalendarTimeViewHeader> T header(int index, Class<T> componentClass) {
        return N2oSelenide.component(element().$$(".rbc-time-header-content").get(index), componentClass);
    }
}
