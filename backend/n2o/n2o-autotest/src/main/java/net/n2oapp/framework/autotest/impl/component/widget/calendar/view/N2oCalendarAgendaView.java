package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarAgendaView;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public class N2oCalendarAgendaView extends N2oComponent implements CalendarAgendaView {

    @Override
    public void shouldHaveSize(int size) {
        rows().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void shouldHaveEventDate(int index, String date) {
        dateCell(index).shouldHave(Condition.text(date));
    }

    @Override
    public void shouldHaveEventTime(int index, String time) {
        timeCell(index).shouldHave(Condition.text(time));
    }

    @Override
    public void shouldHaveEventName(int index, String name) {
        eventCell(index).shouldHave(Condition.text(name));
    }

    private ElementsCollection rows() {
        return element().$$(".rbc-agenda-content .rbc-agenda-table tr");
    }

    private SelenideElement dateCell(int row) {
        return rows().get(row).$(".rbc-agenda-date-cell");
    }

    private SelenideElement timeCell(int row) {
        return rows().get(row).$(".rbc-agenda-time-cell");
    }

    private SelenideElement eventCell(int row) {
        return rows().get(row).$(".rbc-agenda-event-cell");
    }
}
