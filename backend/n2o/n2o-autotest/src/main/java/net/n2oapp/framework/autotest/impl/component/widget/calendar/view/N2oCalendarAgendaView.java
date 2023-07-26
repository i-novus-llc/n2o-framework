package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarAgendaView;
import net.n2oapp.framework.autotest.impl.component.N2oComponent;

import java.time.Duration;

/**
 * Вид отображения календаря 'Повестка дня' для автотестирования
 */
public class N2oCalendarAgendaView extends N2oComponent implements CalendarAgendaView {

    @Override
    public void shouldHaveSize(int size) {
        rows().shouldHave(CollectionCondition.size(size));
    }

    @Override
    public void eventShouldHaveDate(int index, String date, Duration... duration) {
        should(Condition.text(date), dateCell(index), duration);
    }

    @Override
    public void eventShouldHaveTime(int index, String time, Duration... duration) {
        should(Condition.text(time), timeCell(index), duration);
    }

    @Override
    public void eventShouldHaveName(int index, String name, Duration... duration) {
        should(Condition.text(name), eventCell(index), duration);
    }

    protected ElementsCollection rows() {
        return element().$$(".rbc-agenda-content .rbc-agenda-table tr");
    }

    protected SelenideElement dateCell(int row) {
        return rows().get(row).$(".rbc-agenda-date-cell");
    }

    protected SelenideElement timeCell(int row) {
        return rows().get(row).$(".rbc-agenda-time-cell");
    }

    protected SelenideElement eventCell(int row) {
        return rows().get(row).$(".rbc-agenda-event-cell");
    }
}
