package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода даты для автотестирования
 */
public interface DateInput extends Control, PopupControl {

    String getValue();

    void setValue(String value);

    void shouldHavePlaceholder(String value);

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
