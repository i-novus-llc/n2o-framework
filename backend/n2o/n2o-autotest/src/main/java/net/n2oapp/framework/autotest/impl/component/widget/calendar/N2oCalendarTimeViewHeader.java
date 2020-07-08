package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarTimeViewHeader;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

public class N2oCalendarTimeViewHeader extends N2oComponent implements CalendarTimeViewHeader {
    @Override
    public void shouldHaveTitle(String title) {
        element().$(".rbc-row-resource .rbc-header").shouldHave(Condition.text(title));
    }

    @Override
    public void clickAllDay() {
        element().$(".rbc-allday-cell").click();
    }
}
