package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarTimeViewHeader;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Заголовок отображения календаря со временем для автотестирования
 */
public abstract class N2oCalendarTimeViewHeader extends N2oComponent implements CalendarTimeViewHeader {
    @Override
    public void shouldHaveTitle(String title) {
        element().$(".rbc-row-resource .rbc-header").shouldHave(Condition.text(title));
    }
}
