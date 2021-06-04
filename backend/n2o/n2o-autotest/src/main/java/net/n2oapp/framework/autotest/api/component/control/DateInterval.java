package net.n2oapp.framework.autotest.api.component.control;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public interface DateInterval extends Control {

    void beginShouldBeEmpty();

    void endShouldBeEmpty();

    void beginVal(String value);

    void endVal(String value);

    void beginShouldHaveValue(String value);

    void endShouldHaveValue(String value);

    void clickCalendarButton();

    void clickCalendarField();

    void shouldBeBeginActiveDay(String day);

    void shouldBeEndActiveDay(String day);

    void shouldBeDisableEndDay(String day);

    void shouldBeDisableBeginDay(String day);

    void shouldBeEnableBeginDay(String day);

    void shouldBeEnableEndDay(String day);

    void clickBeginDay(String day);

    void clickEndDay(String day);

    void beginShouldHaveCurrentMonth(String month);

    void endShouldHaveCurrentMonth(String month);

    void beginShouldHaveCurrentYear(String year);

    void endShouldHaveCurrentYear(String year);

    void clickBeginPreviousMonthButton();

    void clickEndPreviousMonthButton();

    void clickBeginNextMonthButton();

    void clickEndNextMonthButton();

    void beginTimeVal(String hours, String minutes, String seconds);

    void endTimeVal(String hours, String minutes, String seconds);

    void shouldBeCollapsed();

    void shouldBeExpanded();
}
