package net.n2oapp.framework.autotest.impl.component.widget.calendar.view;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import net.n2oapp.framework.autotest.api.component.widget.calendar.view.CalendarMonthView;

/**
 * Вид отображения календаря 'Месяц' для автотестирования
 */
public class N2oCalendarMonthView extends N2oStandardCalendarView implements CalendarMonthView {
    @Override
    public void shouldBeDayOff(String day) {
        int index = 0;
        for (SelenideElement elm : element().$$(".rbc-date-cell")) {
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
        element().$(".rbc-date-cell.rbc-now").shouldHave(Condition.text(day));
    }

    @Override
    public void clickOnCell(String day) {
        element().$$(".rbc-date-cell").findBy(Condition.text(day)).click();
    }

    @Override
    public void clickOnDay(String day) {
        element().$$(".rbc-date-cell a").findBy(Condition.text(day)).click();
    }
}
