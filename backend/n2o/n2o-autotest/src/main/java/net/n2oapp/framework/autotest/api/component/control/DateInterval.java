package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public interface DateInterval extends Control, PopupControl {

    void shouldHaveEmptyBegin();

    void shouldHaveEmptyEnd();

    void setValueInBegin(String value);

    void setValueInEnd(String value);

    void shouldHaveBeginWithValue(String value);

    void shouldHaveEndWithValue(String value);

    void clickCalendarButton();

    void shouldHaveActiveDayInBegin(String day);

    void shouldHaveActiveDayInEnd(String day);

    void shouldHaveDisableDayInEnd(String day);

    void shouldHaveDisableDayInBegin(String day);

    void shouldHaveEnableDayInBegin(String day);

    void shouldHaveEnableDayInEnd(String day);

    void clickBeginDay(String day);

    void clickEndDay(String day);

    void shouldHaveCurrentMonthInBegin(String month);

    void shouldHaveCurrentMonthInEnd(String month);

    void shouldHaveCurrentYearInBegin(String year);

    void shouldHaveCurrentYearInEnd(String year);

    void clickBeginPreviousMonthButton();

    void clickEndPreviousMonthButton();

    void clickBeginNextMonthButton();

    void clickEndNextMonthButton();

    void setValueInBeginTime(String hours, String minutes, String seconds);

    void setValueEndTime(String hours, String minutes, String seconds);
}
