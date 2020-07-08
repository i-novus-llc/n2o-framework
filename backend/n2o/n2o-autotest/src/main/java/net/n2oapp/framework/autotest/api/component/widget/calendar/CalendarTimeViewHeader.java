package net.n2oapp.framework.autotest.api.component.widget.calendar;

import net.n2oapp.framework.autotest.api.component.Component;

public interface CalendarTimeViewHeader extends Component {
    void shouldHaveTitle(String title);

    void clickAllDay();
}
