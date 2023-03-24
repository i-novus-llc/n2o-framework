package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarMonthView;

/**
 * Вид отображения календаря 'Месяц' для автотестирования
 */
public class N2oCalendarMonthView extends N2oStandardCalendarView implements CalendarMonthView {
    @Override
    public void shouldBeDayOff(String day) {
        int index = 0;
        ElementsCollection dateCells = dateCells();

        for (SelenideElement elm : dateCells) {
            if (elm.getText().equals(day))
                break;
            else
                index++;
        }
        element().$$(".calendar__cell").get(index)
                .shouldHave(Condition.cssClass("calendar__cell--day-off"));
    }

    @Override
    public void shouldBeToday(String day) {
        dateCells().findBy(Condition.cssClass("rbc-now")).shouldHave(Condition.text(day));
    }

    @Override
    public void clickOnCell(String day) {
        dateCells().findBy(Condition.text(day)).click();
    }

    @Override
    public void clickOnDay(String day) {
        dateCells().findBy(Condition.text(day)).$("a").click();
    }

    protected ElementsCollection dateCells() {
        return element().$$(".rbc-date-cell");
    }
}
