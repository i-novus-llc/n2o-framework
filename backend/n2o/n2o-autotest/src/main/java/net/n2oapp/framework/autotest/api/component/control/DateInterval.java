package net.n2oapp.framework.autotest.api.component.control;

/**
 * Поле ввода интервала даты для автотестирования
 */
public interface DateInterval extends Control {

    void setBeginValue(String value);

    void setEndValue(String value);

    void beginShouldHaveValue(String value);

    void endShouldHaveValue(String value);

}
