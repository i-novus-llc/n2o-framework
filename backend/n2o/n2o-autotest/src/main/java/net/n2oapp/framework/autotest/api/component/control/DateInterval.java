package net.n2oapp.framework.autotest.api.component.control;

import net.n2oapp.framework.autotest.api.component.PopupControl;

/**
 * Компонент ввода интервала дат для автотестирования
 */
public interface DateInterval extends Control, PopupControl {

    /**
     * Проверка того, что дата начала не задана
     */
    void shouldHaveEmptyBegin();

    /**
     * Проверка того, что дата конца не задана
     */
    void shouldHaveEmptyEnd();

    /**
     * Установка значения в поле даты начала
     * @param value значение даты в формате день.месяц.год
     */
    void setValueInBegin(String value);

    /**
     * Установка значения в поле даты конца
     * @param value значение даты в формате день.месяц.год
     */
    void setValueInEnd(String value);

    /**
     * Провека того, что в поле даты начала установлено соответствующее значение
     * @param value ожидаемая дата в формате день.месяц.год
     */
    void shouldHaveBeginValue(String value);

    /**
     * Провека того, что в поле даты конца установлено соответствующее значение
     * @param value ожидаемая дата в формате день.месяц.год
     */
    void shouldHaveEndValue(String value);

    /**
     * Клик по иконке календаря в поле ввода для открытия календаря
     */
    void clickCalendarButton();

    /**
     * Проверка выбранности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void shouldHaveBeginActiveDay(String day);

    /**
     * Проверка выбранности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void shouldHaveEndActiveDay(String day);

    /**
     * Проверка недоступности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void shouldHaveEndDisabledDay(String day);

    /**
     * Проверка недоступности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void shouldHaveBeginDisabledDay(String day);

    /**
     * Проверка доступности дня в календаре у поля ввода начала
     * @param day проверяемый день
     */
    void shouldHaveBeginEnabledDay(String day);

    /**
     * Проверка доступности дня в календаре у поля ввода конца
     * @param day проверяемый день
     */
    void shouldHaveEndEnabledDay(String day);

    /**
     * Клик по дню в календаре начала для выбора
     * @param day выбираемый день
     */
    void clickBeginDay(String day);

    /**
     * Клик по дню в календаре начала для выбора
     * @param day выбираемый день
     */
    void clickEndDay(String day);

    /**
     * Проверка текущего месяца в календаре начала
     * @param month ожидаемый текущий месяц
     */
    void shouldHaveBeginCurrentMonth(String month);

    /**
     * Проверка текущего месяца в календаре конца
     * @param month ожидаемый текущий месяц
     */
    void shouldHaveEndCurrentMonth(String month);

    /**
     * Проверка текущего года в календаре начала
     * @param year ожидаемый текущий год
     */
    void shouldHaveBeginCurrentYear(String year);

    /**
     * Проверка текущего года в календаре конца
     * @param year ожидаемый текущий год
     */
    void shouldHaveEndCurrentYear(String year);

    /**
     * Клик по кнопке для перехода к предыдущему месяцу в календаре начала
     */
    void clickBeginPreviousMonthButton();

    /**
     * Клик по кнопке для перехода к предыдущему месяцу в календаре конца
     */
    void clickEndPreviousMonthButton();

    /**
     * Клик по кнопке для перехода к следующему месяцу в календаре начала
     */
    void clickBeginNextMonthButton();

    /**
     * Клик по кнопке для перехода к следующему месяцу в календаре конца
     */
    void clickEndNextMonthButton();

    /**
     * Установка времени в поле ввода даты начала
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void setBeginTimeValue(String hours, String minutes, String seconds);

    /**
     * Установка времени в поле ввода даты конца
     *
     * @param hours часы в формате 24
     * @param minutes минуты
     * @param seconds секунды
     */
    void setEndTimeValue(String hours, String minutes, String seconds);
}
