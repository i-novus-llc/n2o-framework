package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.view.CalendarViewTypeEnum;

/**
 * Панель действий календаря для автотестирования
 */
public interface CalendarToolbar extends Component {
    StandardButton todayButton();

    StandardButton prevButton();

    StandardButton nextButton();

    void shouldHaveLabel(String label);

    StandardButton monthViewButton();

    StandardButton dayViewButton();

    StandardButton agendaViewButton();

    StandardButton weekViewButton();

    StandardButton workWeekButton();

    void shouldHaveActiveView(CalendarViewTypeEnum viewType);
}
