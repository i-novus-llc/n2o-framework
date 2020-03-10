package net.n2oapp.framework.autotest.api.component.control;


/**
 * Компонент ввода даты для автотестирования
 */
public interface DateInput extends Control {

    void shouldHaveValue(String value);

    String val();

    void val(String value);

    void timeVal(String hours, String minutes, String seconds);

    void clickCalendarButton();

    void shouldBeActiveDay(String day);

    void clickDay(String day);

    void shouldBeDisableDay(String day);

    void shouldNotBeDisableDay(String day);

    void shouldHaveCurrentMonth(String month);

    void shouldHaveCurrentYear(String year);

    void clickPreviousMonthButton();

    void clickNextMonthButton();
}
