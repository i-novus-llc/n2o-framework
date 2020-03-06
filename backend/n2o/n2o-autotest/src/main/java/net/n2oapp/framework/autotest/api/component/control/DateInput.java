package net.n2oapp.framework.autotest.api.component.control;


/**
 * Компонент ввода даты для автотестирования
 */
public interface DateInput extends Control {

    void shouldHaveValue(String value);

    String val();

    void val(String value);

    void clickCalendarButton();

    void shouldBeActiveDay(String day);
}
