package net.n2oapp.framework.autotest.impl.component.widget.calendar;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.widget.calendar.CalendarToolbar;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;
import net.n2oapp.framework.autotest.impl.component.widget.calendar.view.CalendarViewType;

/**
 * Панель действий календаря для автотестирования
 */
public class N2oCalendarToolbar extends N2oComponent implements CalendarToolbar {
    @Override
    public StandardButton todayButton() {
        return button("Сегодня");
    }

    @Override
    public StandardButton prevButton() {
        return button("Назад");
    }

    @Override
    public StandardButton nextButton() {
        return button("Вперед");
    }

    @Override
    public void shouldHaveLabel(String label) {
        element().$(".rbc-toolbar-label").shouldHave(Condition.text(label));
    }

    @Override
    public StandardButton monthViewButton() {
        return button("Месяц");
    }

    @Override
    public StandardButton dayViewButton() {
        return button("День");
    }

    @Override
    public StandardButton agendaViewButton() {
        return button("Повестка дня");
    }

    @Override
    public StandardButton weekViewButton() {
        return button("Неделя");
    }

    @Override
    public StandardButton workWeekButton() {
        return button("Рабочая неделя");
    }

    @Override
    public void shouldHaveActiveView(CalendarViewType viewType) {
        element().$(".rbc-active").shouldHave(Condition.text(viewType.getTitle()));
    }

    protected StandardButton button(String label) {
        return N2oSelenide.component(element().$$("button").findBy(Condition.text(label)), StandardButton.class);
    }
}
